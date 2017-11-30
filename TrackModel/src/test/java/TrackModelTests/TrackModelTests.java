package TrackModelTests;

import TrackModel.Models.Block;
import TrackModel.Models.BlockType;
import TrackModel.Models.Line;
import TrackModel.TrackModel;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TrackModelTests {
    @Test
    public void AddingMultipleBlocks_IndexesCorrect() {
        // Arrange
        Block blockAtIndex2 = new Block(2, Line.GREEN, BlockType.STANDARD, 0, 0, new ArrayList<>(), 0, 0, false, false, 0, 0);
        Block blockAtIndex3 = new Block(3, Line.GREEN, BlockType.STANDARD, 0, 0, new ArrayList<>(), 0, 0, false, false, 0, 0);

        TrackModel trackModel = new TrackModel();

        // Act
        trackModel.addBlock(blockAtIndex3);
        trackModel.addBlock(blockAtIndex2);

        // Assert
        assertEquals(blockAtIndex3, trackModel.getBlock(blockAtIndex3.getLine(), 3));
        assertEquals(blockAtIndex2, trackModel.getBlock(blockAtIndex2.getLine(), 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void AddingNullBlock_ExceptionThrown() {
        // Arrange
        Block block = null;

        TrackModel trackModel = new TrackModel();

        // Act
        trackModel.addBlock(block);
    }
}
