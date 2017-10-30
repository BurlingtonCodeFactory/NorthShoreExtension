/*
 * Copyright (c) 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package TrackController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import TrackController.Models.Block;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import TrackController.Models.*;

public class Main extends Application {
    static List<TrackController> trackControllers;
    static HashMap<Block, Integer> blockMapping;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        trackControllers = new ArrayList<>();
        blockMapping = new HashMap<>();

        //add blocks to controllers
        Block a = new Block(0, Line.GREEN,
                BlockType.STANDARD, 500, 0 ,false, new ArrayList<Block>(), true);
        Block b = new Block(1, Line.GREEN,
                BlockType.CROSSING, 500, 0 ,false, new ArrayList<Block>(), true);
        Block c = new Block(2, Line.GREEN,
                BlockType.STANDARD, 500, 0 ,false, new ArrayList<Block>(), true);
        Block d = new Block(3, Line.GREEN,
                BlockType.SWITCH, 500, 0 ,false, new ArrayList<Block>(), true);
        Block e = new Block(4, Line.GREEN,
                BlockType.STANDARD, 500, 0 ,false, new ArrayList<Block>(), true);
        Block f = new Block(5, Line.GREEN,
                BlockType.STANDARD, 500, 0 ,false, new ArrayList<Block>(), true);

        a.rightNeighbor = b;
        a.leftNeighbor = null;
        b.rightNeighbor = c;
        b.leftNeighbor = a;
        c.leftNeighbor = b;
        c.rightNeighbor = d;
        d.leftNeighbor = c;
        d.switchZero = e;
        d.switchOne = f;
        d.rightNeighbor = f;
        d.switchState = false;



        trackControllers.add(new TrackController(0, "TrackController1", "controller1.PLC"));
        trackControllers.get(0).addBlock(a);
        trackControllers.get(0).addBlock(b);
        trackControllers.get(0).addBlock(c);
        trackControllers.get(0).addBlock(d);
        trackControllers.get(0).addBlock(e);
        trackControllers.get(0).addBlock(f);

        List<Block> testAuthority = new ArrayList<>();
        testAuthority.add(b);
        testAuthority.add(c);
        testAuthority.add(d);
        testAuthority.add(e);


        Application.launch(Main.class, (java.lang.String[])null);

    }

    @Override
    public void start(Stage primaryStage) {
        try {
            AnchorPane page = FXMLLoader.load(Main.class.getResource("../../../resources/main/fxml/TrackController.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Track Controller");
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

