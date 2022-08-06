# Cloud 2D Game Engine

<div style="width: 100%; display: flex; justify-content: center;">
    <img src="logo.png" alt="isolated" width="400"/>
</div>

## What is Cloud?

Cloud is a 2D game engine for Java. It's using [LWJGL]() under the hood. Currently it's still in an early phase and should only be used for small projects.

## What's possible?

[Here]() you can find some demo projects.

The main focus of Cloud is to make it easier to work with LWJGL.

It's mainly a wrapper for the basic functionality of LWJGL.
Which means, if a feature is missing you can easily add it to the engine by either forking the repository and change the engine code or add a feature directly in your project.

## What's included

Physics engine [Box2D]()
Particle system
Font renderer
Rendering utilities
UI system and components

## What's missing?

Batch rendering (Has been removed in the latest version!)
TileSet support (For game easier gameworld design)
Level builder
Gizmos

## Current issues

Cloud currenlty uses AWT classes. This causes the engine to not work
properly on Mac OS devices. This will be fixed when all AWT classes are replaced with the STB alternative.

Rendering is currently not optimized, because batch rendering is currently not supported. This will also be fixed in a later release.

If you have an issue not listed here, please create an issue on the repository issues page [here]().

## How can I get started?

Download the compiled jar file [here](), or clone the repository.

Then add the jar file to your projects class path, or import the cloned repository.

```java
public static void main(String[] args) {
    // Create a new window with a size of 1280x720
    Window window = new Window(1280, 720, "Demo");

    // Enable vsync
    window.useVsync(true);

    // Show the window and start the game loop
    try {
        window.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

When you run your application a window with the specified resolution should appear.

Currently there will be a blank black screen. That's because we currently don't have a scene set where content could be drawn.

To change that we will create a scene and add it to the window.

Create a new class, it will extends the `Scene` class from the engine. This will be the place where your content will be drawn.

```java
package de.kostari.demo;

import de.kostari.cloud.core.scene.Scene;
import de.kostari.cloud.core.window.Window;

public class MainScene extends Scene {

    public MainScene(Window window) {
        super(window);
    }

}
```

Now you just have to add it to the window at your window variable.

```java
window.setScene(YOUR_CLASS_NAME.class);
```

When you start your application now nothing seems to be changed, but now we can draw content to the window.

In your scene class add a draw method and draw a rectangle to the screen using the `Render` utility class.

```java
@Override
public void draw(float delta) {
    Render.rect(window.getHalfSize(), 100, 100, CColor.WHITE);
    super.draw(delta);
}
```

If everything worked and you did everything correctly
you should see a white rectangle in the center of your window.

If you want to learn more go head to the documentation [here](https://iotacb.github.io/docs.cloud/).
