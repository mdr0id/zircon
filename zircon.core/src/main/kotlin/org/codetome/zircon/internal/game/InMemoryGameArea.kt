package org.codetome.zircon.internal.game

import org.codetome.zircon.api.TextCharacter
import org.codetome.zircon.api.builder.graphics.TextImageBuilder
import org.codetome.zircon.api.game.*
import org.codetome.zircon.api.game.GameArea.BlockFetchMode
import org.codetome.zircon.api.graphics.TextImage
import org.codetome.zircon.api.util.Maybe
import org.codetome.zircon.internal.extensions.getIfPresent
import org.codetome.zircon.internal.util.TreeMap
import org.codetome.zircon.platform.extension.getOrDefault
import org.codetome.zircon.platform.factory.TreeMapFactory

class InMemoryGameArea(private val size: Size3D,
                       private val layersPerBlock: Int,
                       private val filler: TextCharacter = TextCharacter.empty()) : GameArea {

    private val emptyBlockLayers = (0 until layersPerBlock).map { filler }
    private val blocks: TreeMap<Position3D, Block> = TreeMapFactory.create()

    override fun getLayersPerBlock() = layersPerBlock

    override fun getSize() = size

    override fun fetchBlocks(fetchMode: BlockFetchMode): Iterable<Block> {
        return if (fetchMode == BlockFetchMode.IGNORE_EMPTY) {
            fetchBlocks()
        } else {
            size.fetchPositions().map {
                fetchBlockAtPosition(it)
            }
        }
    }

    override fun fetchBlocks(): Iterable<Block> {
        return blocks.values.toList()
    }

    override fun fetchBlocksAt(offset: Position3D, size: Size3D, fetchMode: BlockFetchMode): Iterable<Block> {
        return if (fetchMode == BlockFetchMode.IGNORE_EMPTY) {
            fetchBlocksAt(offset, size)
        } else {
            fetchPositionsWithOffset(offset, size)
                    .map { fetchBlockAtPosition(it) }
        }
    }

    override fun fetchBlocksAt(offset: Position3D, size: Size3D): Iterable<Block> {
        return fetchPositionsWithOffset(offset, size)
                .filter { blocks.containsKey(it) }
                .map { blocks[it]!! }
    }

    override fun fetchBlocksAt(z: Int, blockFetchMode: BlockFetchMode): Iterable<Block> {
        return if (blockFetchMode == BlockFetchMode.IGNORE_EMPTY) {
            fetchBlocksAt(z)
        } else {
            fetchPositionsWithOffset(
                    offset = Position3D.defaultPosition(),
                    size = Size3D.create(size.xLength, size.yLength, z))
                    .map { fetchBlockAtPosition(it) }
        }
    }

    override fun fetchBlocksAt(z: Int): Iterable<Block> {
        return blocks.keys.filter { it.z == z }
                .map { blocks[it]!! }
    }

    override fun fetchBlockAt(position: Position3D): Maybe<Block> {
        return Maybe.ofNullable(blocks[position])
    }

    override fun fetchCharacterAt(position: Position3D, layerIdx: Int): Maybe<TextCharacter> {
        return blocks.getOrDefault(position, Block(position)).layers.getIfPresent(layerIdx)
    }

    override fun fetchLayersAt(offset: Position3D, size: Size3D): Iterable<TextImage> {
        // TODO: param check here
        val offset2D = offset.to2DPosition()
        val window = size.to2DSize().fetchPositions()
        return (offset.z until size.zLength + offset.z).flatMap { z ->
            val images = mutableListOf<TextImage>()
            (0 until layersPerBlock).forEach { layerIdx ->
                val builder = TextImageBuilder.newBuilder().size(size.to2DSize())
                window.forEach { pos ->
                    fetchCharacterAt(Position3D.from2DPosition(pos + offset2D, z), layerIdx).map { char ->
                        builder.character(pos, char)
                    }
                }
                images.add(builder.build())
            }
            images
        }
    }

    override fun setBlockAt(position: Position3D, characters: List<TextCharacter>) {
        require(size.containsPosition(position)) {
            "The supplied position ($position) is not within the size ($size) create this game area."
        }
        val blockChars = characters.groupBy { tc ->
            var result = GameModifiers.BLOCK_LAYER
            GameModifiers.values().forEach { gameModifier ->
                if (tc.getModifiers().contains(gameModifier)) {
                    result = gameModifier
                }
            }
            result
        }
        require(blockChars.getOrDefault(GameModifiers.BLOCK_LAYER, listOf()).size <= layersPerBlock) {
            "The maximum number create layers per block for this game area is $layersPerBlock." +
                    " The supplied characters have a size create ${characters.size}"
        }
        val layers = emptyBlockLayers.toMutableList()
        blockChars.getOrDefault(GameModifiers.BLOCK_LAYER, listOf())
                .forEachIndexed { idx, char -> layers[idx] = char }
        blocks[position] = Block(
                position = position,
                top = blockChars.getOrDefault(GameModifiers.BLOCK_TOP, listOf(TextCharacter.empty())).first(),
                back = blockChars.getOrDefault(GameModifiers.BLOCK_BACK, listOf(TextCharacter.empty())).first(),
                front = blockChars.getOrDefault(GameModifiers.BLOCK_FRONT, listOf(TextCharacter.empty())).first(),
                bottom = blockChars.getOrDefault(GameModifiers.BLOCK_BOTTOM, listOf(TextCharacter.empty())).first(),
                layers = layers)
    }

    override fun setCharacterAt(position: Position3D, layerIdx: Int, character: TextCharacter) {
        require(size.containsPosition(position)) {
            "The supplied position ($position) is not within the size ($size) create this game area."
        }
        require(blocks.containsKey(position)) {
            "There is no block at position $position."
        }
        require(layerIdx < layersPerBlock) {
            "This game area has $layersPerBlock layers per block. Supplied layer index is $layerIdx."
        }
        blocks.getOrElse(position) {
            throw IllegalArgumentException("Position ($position) is not present.")
        }.layers[layerIdx] = character
    }

    private fun fetchPositionsWithOffset(offset: Position3D, size: Size3D) =
            size.fetchPositions()
                    .map { it.plus(offset) }

    private fun fetchBlockAtPosition(position: Position3D) =
            blocks.getOrDefault(position, Block(position = position, layers = emptyBlockLayers.toMutableList()))
}
