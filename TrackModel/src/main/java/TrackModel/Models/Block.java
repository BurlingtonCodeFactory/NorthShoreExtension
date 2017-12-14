package TrackModel.Models;

import TrackModel.Events.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private int id;
    private Line line;
    private BlockType blockType;

    private int beacon;
    private boolean circuitFailed;
    private double coefficientFriction;
    private List<Block> commandedAuthority;
    private double commandedSpeed;
    private List<Integer> connectedBlocks;
    private double elevation;
    private boolean failed;
    private double grade;
    private boolean heaterOn;
    private boolean isBidirectional;
    private boolean isOccupied;
    private boolean isUnderground;
    private double length;
    private boolean lightGreen;
    private boolean powerFailed;
    private boolean railBroken;
    private double speedLimit;
    private List<Block> suggestedAuthority;
    private double suggestedSpeed;
    private boolean suggestMaintenance;
    private boolean underMaintenance;
    private Boolean lock;

    private static List<OccupancyChangeListener> occupancyChangeListeners = new ArrayList<>();
    private static List<SuggestedSpeedChangeListener> suggestedSpeedChangeListeners = new ArrayList<>();
    private static List<SuggestedAuthorityChangeListener> suggestedAuthorityChangeListeners = new ArrayList<>();
    private static List<FailureChangeListener> failureChangeListeners = new ArrayList<>();
    private static List<SwitchStateManualChangeListener> switchStateManualChangeListeners = new ArrayList<>();
    private static List<SwitchStateChangeListener> switchStateChangeListeners = new ArrayList<>();
    private static List<MaintenanceRequestListener> maintenanceRequestListeners = new ArrayList<>();
    private static List<MaintenanceChangeListener> maintenanceChangeListeners = new ArrayList<>();

    public Block(int id, Line line, BlockType blockType, int beacon, double coefficientFriction, List<Integer> connectedBlocks, double elevation, double grade, boolean isBidirectional, boolean isUnderground, double length, double speedLimit){
        this.id = id;
        this.line = line;
        this.blockType = blockType;
        this.beacon = beacon;
        this.circuitFailed = false;
        this.coefficientFriction = coefficientFriction;
        this.commandedAuthority = new ArrayList<>();
        this.commandedSpeed = 0;
        this.connectedBlocks = connectedBlocks;
        this.elevation = elevation;
        this.failed = false;
        this.grade = grade;
        this.heaterOn = false;
        this.isBidirectional = isBidirectional;
        this.isOccupied = false;
        this.isUnderground = isUnderground;
        this.length = length;
        this.lightGreen = true;
        this.powerFailed = false;
        this.railBroken = false;
        this.speedLimit = speedLimit;
        this.suggestedAuthority = new ArrayList<>();
        this.suggestedSpeed = 0;
        this.suggestMaintenance = false;
        this.underMaintenance = false;
        this.lock = null;
    }

    //<editor-fold desc="Getters">

    public int getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public int getBeacon() {
        return beacon;
    }

    public boolean getCircuitFailed() {
        return circuitFailed;
    }

    public double getCoefficientFriction() {
        return coefficientFriction;
    }

    public List<Block> getCommandedAuthority() {
        return commandedAuthority;
    }

    public double getCommandedSpeed() {
        return commandedSpeed;
    }

    public List<Integer> getConnectedBlocks() {
        return connectedBlocks;
    }

    public double getElevation() {
        return elevation;
    }

    public boolean getFailed() {
        return failed;
    }

    public double getGrade() {
        return grade;
    }

    public boolean getHeaterOn() {
        return heaterOn;
    }

    public boolean getIsBidirectional() {
        return isBidirectional;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public boolean getIsUnderground() {
        return isUnderground;
    }

    public double getLength() {
        return length;
    }

    public boolean getLightGreen() {
        return lightGreen;
    }

    public boolean getPowerFailed() {
        return powerFailed;
    }

    public boolean getRailBroken() {
        return railBroken;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public List<Block> getSuggestedAuthority() {
        return suggestedAuthority;
    }

    public double getSuggestedSpeed() {
        return suggestedSpeed;
    }

    public boolean getSuggestMaintenance() {
        return suggestMaintenance;
    }

    public boolean getUnderMaintenance() {
        return underMaintenance;
    }

    public Boolean getLock(){ return lock; }

    public String getCommandedAuthorityString(){
        if(commandedAuthority.size() == 0)
        {
            return "";
        }

        String authority = "";
        for(Block block : commandedAuthority)
        {
            authority += block.getId()+",";
        }
        return authority.substring(0, authority.length()-1);
    }

    public String getSuggestedAuthorityString(){
        if(suggestedAuthority.size() == 0)
        {
            return "";
        }

        String authority = "";
        for(Block block : suggestedAuthority)
        {
            authority += block.getId()+",";
        }
        return authority.substring(0, authority.length()-1);
    }

    public String getConnectedBlocksString()
    {
        if(connectedBlocks.size() == 0)
        {
            return "";
        }

        String blocks = "";
        for(int block : connectedBlocks)
        {
            blocks += block+",";
        }
        return blocks.substring(0, blocks.length()-1);
    }


    public int getNextBlock() {
        int maxID = -1;
        for (int block : connectedBlocks) {
            if(block > maxID)
            {
                maxID = block;
            }
        }
        return maxID;
    }

    public int getPreviousBlock() { return id-1; }

    //</editor-fold>

    //<editor-fold desc="Setters">

        public void setCircuitFailed(boolean circuitFailed) {
            this.circuitFailed = circuitFailed;
            updateFailure();
        }

        public void setCoefficientFriction(double coefficientFriction) {
            this.coefficientFriction = coefficientFriction;
        }

        public void setCommandedAuthority(List<Block> commandedAuthority) {
            this.commandedAuthority = commandedAuthority;
        }

        public void setCommandedSpeed(double commandedSpeed) {
            this.commandedSpeed = commandedSpeed;
        }

        public void setFailed(boolean failed) {
            this.failed = failed;
            fireFailureChangeEvent(this);
        }

        public void setHeaterOn(boolean heaterOn) {
            this.heaterOn = heaterOn;
        }

        public void setIsOccupied(boolean isOccupied) {
            this.isOccupied = isOccupied;
            fireOccupancyChangeEvent(this);
        }

        public void setLightGreen(boolean lightGreen) {
            this.lightGreen = lightGreen;
        }

        public void setPowerFailed(boolean powerFailed) {
            this.powerFailed = powerFailed;
            updateFailure();
        }

        public void setRailBroken(boolean railBroken) {
            this.railBroken = railBroken;
            updateFailure();
        }

        public void updateFailure()
        {
            this.failed = railBroken || powerFailed || circuitFailed;
            fireFailureChangeEvent(this);
        }

        public void setSuggestedAuthority(List<Block> suggestedAuthority) {
            this.suggestedAuthority = suggestedAuthority;
            fireSuggestedAuthorityChangeEvent(this);

        }

        public void setSuggestedSpeed(double suggestedSpeed) {
            this.suggestedSpeed = suggestedSpeed;
            fireSuggestedSpeedChangeEvent(this);
        }

        public void setSuggestMaintenance(boolean suggestMaintenance) {
            this.suggestMaintenance = suggestMaintenance;
            fireMaintenanceRequestEvent(this);
        }

        public void setUnderMaintenance(boolean underMaintenance) {
            if (this.underMaintenance != underMaintenance) {
                this.underMaintenance = underMaintenance;
                fireMaintenanceChangeEvent(this);
            }
            this.underMaintenance = underMaintenance;
        }

        public void setLock(boolean lock)
        {
            if(this.lock != null)
            {
                this.lock = lock;
            }
        }

        //</editor-fold>

    @Override
    public String toString() {
        return blockType == BlockType.STATION ? ((Station)this).getStationName() : Integer.toString(getId());
    }

    public void createLock()
    {
        this.lock = new Boolean(false);
    }

    public boolean hasLock()
    {
        return this.lock != null;
    }

    // Events

    // Occupancy Change
    public static synchronized void addOccupancyChangeListener( OccupancyChangeListener l ) {
        occupancyChangeListeners.add( l );
    }

    public static synchronized void removeOccupancyChangeListener( OccupancyChangeListener l ) {
        occupancyChangeListeners.remove( l );
    }

    private static synchronized void fireOccupancyChangeEvent(Object source)
    {
        System.out.println("Fire occupancy change");
        OccupancyChangeEvent event = new OccupancyChangeEvent(source);
        for(OccupancyChangeListener listener : occupancyChangeListeners)
        {
            Platform.runLater(
                    () -> listener.occupancyChangeReceived(event)
            );
        }
    }

    // Suggested Speed Change
    public static synchronized void addSuggestedSpeedChangeListener( SuggestedSpeedChangeListener l ) {
        suggestedSpeedChangeListeners.add( l );
    }

    public static synchronized void removeSuggestedSpeedChangeListener( SuggestedSpeedChangeListener l ) {
        suggestedSpeedChangeListeners.remove( l );
    }

    private static synchronized void fireSuggestedSpeedChangeEvent(Object source)
    {
        System.out.println("Fire suggested speed change");
        SuggestedSpeedChangeEvent event = new SuggestedSpeedChangeEvent(source);
        for(SuggestedSpeedChangeListener listener : suggestedSpeedChangeListeners)
        {
            listener.suggestedSpeedChangeReceived(event);
        }
    }

    // Suggested Speed Change
    public static synchronized void addSuggestedAuthorityChangeListener( SuggestedAuthorityChangeListener l ) {
        suggestedAuthorityChangeListeners.add( l );
    }

    public static synchronized void removeSuggestedAuthorityChangeListener( SuggestedAuthorityChangeListener l ) {
        suggestedAuthorityChangeListeners.remove( l );
    }

    private static synchronized void fireSuggestedAuthorityChangeEvent(Object source)
    {
        System.out.println("Fire suggested authority change");

        SuggestedAuthorityChangeEvent event = new SuggestedAuthorityChangeEvent(source);
        for(SuggestedAuthorityChangeListener listener : suggestedAuthorityChangeListeners)
        {
            listener.suggestedAuthorityChangeReceived(event);
        }
    }

    // Failure Change
    public static synchronized void addFailureChangeListener( FailureChangeListener l ) {
        failureChangeListeners.add( l );
    }

    public static synchronized void removeFailureChangeListener( FailureChangeListener l ) {
        failureChangeListeners.remove( l );
    }

    private static synchronized void fireFailureChangeEvent(Object source)
    {
        System.out.println("Fire suggested failure change");

        FailureChangeEvent event = new FailureChangeEvent(source);
        for(FailureChangeListener listener : failureChangeListeners)
        {
            listener.failureChangeReceived(event);
        }
    }

    // Manual Switch Change
    public static synchronized void addSwitchStateManualChangeListener(SwitchStateManualChangeListener l ) {
        switchStateManualChangeListeners.add( l );
    }

    public static synchronized void removeSwitchStateManualChangeListener(SwitchStateManualChangeListener l ) {
        switchStateManualChangeListeners.remove( l );
    }

    protected static synchronized void fireSwitchStateManualChangeEvent(Object source)
    {
        System.out.println("Fire manual switch change");

        SwitchStateManualChangeEvent event = new SwitchStateManualChangeEvent(source);
        for(SwitchStateManualChangeListener listener : switchStateManualChangeListeners)
        {
            listener.switchStateManualChangeReceived(event);
        }
    }

    // Switch Change
    public static synchronized void addSwitchStateChangeListener( SwitchStateChangeListener l ) {
        switchStateChangeListeners.add( l );
    }

    public static synchronized void removeSwitchStateChangeListener( SwitchStateChangeListener l ) {
        switchStateChangeListeners.remove( l );
    }

    protected static synchronized void fireSwitchStateChangeEvent(Object source)
    {
        SwitchStateChangeEvent event = new SwitchStateChangeEvent(source);
        for(SwitchStateChangeListener listener : switchStateChangeListeners)
        {
            listener.switchStateChangeReceived(event);
        }
    }

    // Request Maintenance Change
    public static synchronized void addMaintenanceRequestListener( MaintenanceRequestListener l ) {
        maintenanceRequestListeners.add( l );
    }

    public static synchronized void removeMaintenanceRequestListener( MaintenanceRequestListener l ) {
        maintenanceRequestListeners.remove( l );
    }

    protected static synchronized void fireMaintenanceRequestEvent(Object source)
    {
        System.out.println("Fire suggested maintenance change");

        MaintenanceRequestEvent event = new MaintenanceRequestEvent(source);
        for(MaintenanceRequestListener listener : maintenanceRequestListeners)
        {
            listener.maintenanceRequestReceived(event);
        }
    }

    // Maintenance Change
    public static synchronized void addMaintenanceChangeListener( MaintenanceChangeListener l ) {
        maintenanceChangeListeners.add( l );
    }

    public static synchronized void removeMaintenanceChangeListener( MaintenanceChangeListener l ) {
        maintenanceChangeListeners.remove( l );
    }

    protected static synchronized void fireMaintenanceChangeEvent(Object source)
    {
        //System.out.println("Fire maintenance change");

        MaintenanceChangeEvent event = new MaintenanceChangeEvent(source);
        for(MaintenanceChangeListener listener : maintenanceChangeListeners)
        {
            listener.maintenanceChangeReceived(event);
        }
    }
}
