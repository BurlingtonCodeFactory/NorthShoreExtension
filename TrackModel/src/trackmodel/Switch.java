package trackmodel;

public class Switch extends Block {
    private boolean switchState;
    private Block switchZero;
    private Block switchOne;

    public Switch(int number, Line line, BlockType infra, int size, Block previousBlock, Block switchZero, Block switchOne)
    {
        super(number, line, infra, size, previousBlock, switchZero);
        this.switchState = false;
        this.switchZero = switchZero;
        this.switchOne = switchOne;
    }

    //<editor-fold desc="Getters">

    public boolean getSwitchState()
    {
        return switchState;
    }

    public Block getSwitchZero()
    {
        return switchZero;
    }

    public Block getSwitchOne()
    {
        return switchOne;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">

    public void setSwitchState(boolean newSwitchState)
    {
        this.switchState = newSwitchState;
        this.setNextBlock(newSwitchState ? switchOne : switchZero);
    }

    //</editor-fold>
}
