# Catan Game

A Java-based implementation of the popular board game Catan, built using LibGDX framework.

## Overview

This project implements a digital version of Settlers of Catan, a strategic board game where players compete to build settlements, roads, and cities on an island. The game features resource management, trading mechanics, and dynamic gameplay.

## Project Structure

The project is organized as a multi-module Gradle build with the following components:

- **core**: Core game logic including:
  - Game mechanics and rules
  - Map generation and management
  - Player management
  - Server/networking functionality
  
- **desktop**: Desktop client application using LibGDX

- **assets**: Game resources including:
  - Backgrounds and UI textures
  - Game cards and indicators
  - Resource tokens
  - Road and village graphics
  - Game rule reference images

## Technologies

- **Language**: Java
- **Build System**: Gradle
- **Game Framework**: LibGDX 1.12.0
- **Physics**: Box2D Lights

## Building the Project

### Prerequisites

- Java JDK 8 or higher
- Gradle (or use the included gradlew wrapper)

### Build Instructions

```bash
# Build the entire project
./gradlew build

# Build desktop application
./gradlew desktop:build

# Run the desktop application
./gradlew desktop:run
```

## Project Modules

### Core Module
Located in `core/src/com/game/catan/`, contains:
- **Functionality**: Game rules and mechanics
- **Map**: Game board and tile management
- **Player**: Player state and management
- **Server**: Game server logic

### Desktop Module
Located in `desktop/src/com/game/catan/`, contains the desktop UI and game client implementation.

## Assets

All game assets are located in the `assets/` directory:
- Game backgrounds and UI elements
- Resource and building cards
- Player indicators for different colors (blue, red, green, yellow)
- Game rules reference images
- Texture assets for buttons, robber, and trading interface

## Features

- Multi-player gameplay support
- Resource trading system
- Building and settlement mechanics
- Road construction
- Dice rolling and turn-based gameplay
- Visual indicators for player status and actions

## Development

The project uses a client-server architecture with:
- Core game logic separated from UI
- LibGDX for rendering and game loop management
- Modular asset system for easy resource management

## License

[Add appropriate license information here]

## Contributing

[Add contribution guidelines here]

