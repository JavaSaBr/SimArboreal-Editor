/*
 * $Id$
 * 
 * Copyright (c) 2014, Simsilica, LLC
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions 
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in 
 *    the documentation and/or other materials provided with the 
 *    distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its 
 *    contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
package com.simsilica.arboreal;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.style.StyleLoader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Main class for the TreeEditor.
 *
 *  @author PSpeed
 */
public class TreeEditor extends SimpleApplication {

    static Logger log = LoggerFactory.getLogger(TreeEditor.class);

    public static final String GLASS_STYLES = "/com/simsilica/arboreal/ui/glass-styles.groovy";
 
    public static void main( String... args ) {        
        TreeEditor main = new TreeEditor();
        
        AppSettings settings = new AppSettings(false);
        settings.setTitle("SimArboreal Tree Editor");
        settings.setSettingsDialogImage("/com/simsilica/arboreal/images/TreeEditor-Splash.png");
        
        try {
            BufferedImage[] icons = new BufferedImage[] {
                    ImageIO.read( TreeEditor.class.getResource( "/com/simsilica/arboreal/images/TreeEditor-icon-128.png" ) ),
                    ImageIO.read( TreeEditor.class.getResource( "/com/simsilica/arboreal/images/TreeEditor-icon-32.png" ) ),
                    ImageIO.read( TreeEditor.class.getResource( "/com/simsilica/arboreal/images/TreeEditor-icon-16.png" ) )
                };
            settings.setIcons(icons);
        } catch( IOException e ) {
            log.warn( "Error loading globe icons", e );
        }        
        
        main.setSettings(settings);
        
        main.start();
    }
    
    public TreeEditor() {
        super(new StatsAppState(), new DebugKeysAppState(),
              new BuilderState(1, 1), 
              new MovementState(),
              new DebugHudState(),
              new LightingState(),
              new GroundState(),
              new SkyState(),
              new AvatarState(),
              new TreeOptionsState(),
              new TreeParametersState(),
              new ForestGridState(),
              new ScreenshotAppState("", System.currentTimeMillis())); 
    }
 
    @Override
    public void simpleInitApp() {
    
        GuiGlobals.initialize(this);

        cam.setLocation(new Vector3f(0, 1.8f, 10));

        InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();
        MainFunctions.initializeDefaultMappings(inputMapper);
        inputMapper.activateGroup( MainFunctions.GROUP );        
        MovementFunctions.initializeDefaultMappings(inputMapper);

        /*
        // Now create the normal simple test scene    
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", ColorRGBA.Blue);
        mat.setColor("Ambient", ColorRGBA.Green);
        mat.setBoolean("UseMaterialColors", true);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
        */ 

        new StyleLoader(GuiGlobals.getInstance().getStyles()).loadStyleResource(GLASS_STYLES);
 
        TreeOptionsState treeOptions = stateManager.getState(TreeOptionsState.class);                
        treeOptions.addOptionToggle("Show Grass", stateManager.getState(GroundState.class), "setShowGrass");                
        treeOptions.addOptionToggle("Show Sky", stateManager.getState(SkyState.class), "setShowSky");                
    }        
}
