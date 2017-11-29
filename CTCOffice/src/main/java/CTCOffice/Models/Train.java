package CTCOffice.Models;

import TrackModel.Models.Block;
import TrackModel.Models.Line;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Train {
    private int id;
    private Line line;
    private Block previousBlock;
    private Block currentBlock;
    private int suggestedSpeed;
    private List<Block> suggestedAuthority;
    private List<Stop> schedule;

    private ObjectProperty<Block> previousBlockProperty;
    private ObjectProperty<Block> currentBlockProperty;
    private IntegerProperty suggestedSpeedProperty;
    private ListProperty<Block> suggestedAuthorityProperty;
    private ListProperty<Stop> scheduleProperty;

    public Train(int id, Line line, Block previousBlock, Block currentBlock) {
        this.id = id;
        this.line = line;
        this.previousBlockProperty = new SimpleObjectProperty<>();
        this.currentBlockProperty = new SimpleObjectProperty<>();
        this.suggestedSpeedProperty = new SimpleIntegerProperty();
        this.suggestedAuthorityProperty = new SimpleListProperty<>();
        this.scheduleProperty = new SimpleListProperty<>();

        setPreviousBlock(previousBlock);
        setCurrentBlock(currentBlock);
        setSuggestedSpeed(0);
        setSuggestedAuthority(new ArrayList<>());
        setSchedule(new ArrayList<>());
    }

    //<editor-fold desc="Getters">

    public int getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Block getPreviousBlock() {
        return previousBlock;
    }

    public ObjectProperty<Block> getPreviousBlockProperty() {
        return previousBlockProperty;
    }

    public Block getCurrentBlock() {
        return currentBlock;
    }

    public ObjectProperty<Block> getCurrentBlockProperty() {
        return currentBlockProperty;
    }

    public int getSuggestedSpeed() {
        return suggestedSpeed;
    }

    public IntegerProperty getSuggestedSpeedProperty() {
        return suggestedSpeedProperty;
    }

    public List<Block> getSuggestedAuthority() {
        return suggestedAuthority;
    }

    public ListProperty<Block> getSuggestedAuthorityProperty() {
        return suggestedAuthorityProperty;
    }

    public List<Stop> getSchedule() {
        return schedule;
    }

    public ObservableList<Stop> getScheduleProperty() {
        return scheduleProperty;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">

    public void setPreviousBlock(Block previousBlock) {
        this.previousBlock = previousBlock;
        this.previousBlockProperty.setValue(previousBlock);
    }

    public void setCurrentBlock(Block currentBlock) {
        this.currentBlock = currentBlock;
        this.currentBlockProperty.setValue(currentBlock);
    }

    public void setSuggestedSpeed(int suggestedSpeed) {
        this.suggestedSpeed = suggestedSpeed;
        this.suggestedSpeedProperty.setValue(suggestedSpeed);
    }

    public void setSuggestedAuthority(List<Block> suggestedAuthority) {
        this.suggestedAuthority = suggestedAuthority;
        this.suggestedAuthorityProperty.setValue(FXCollections.observableArrayList(suggestedAuthority));
    }

    public void setSchedule(List<Stop> schedule) {
        this.schedule = schedule;
        this.scheduleProperty.setValue(FXCollections.observableArrayList(schedule));
    }

    //</editor-fold>

    public void addStop(Stop stop) {
        scheduleProperty.getValue().add(stop);
        schedule.add(stop);
    }

    public void removeStop(Stop stop) {
        scheduleProperty.getValue().remove(stop);
        schedule.remove(stop);
    }

    @Override
    public String toString() {
        return line + "-" + id;
    }
}
