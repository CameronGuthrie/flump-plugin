# **Flump Plugin for RuneLite**

## **Table of Contents**

- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
  - [Commands](#commands)
  - [Overlays](#overlays)
- [Development](#development)
  - [Project Structure](#project-structure)
  - [Building the Plugin](#building-the-plugin)
  - [Testing](#testing)
- [Contribution](#contribution)
- [License](#license)
- [Disclaimer](#disclaimer)
- [Contact](#contact)

---

## **Overview**

The **Flump Plugin** is a custom plugin developed for the RuneLite client, aiming to enhance the Old School RuneScape gameplay experience by providing advanced automation features, interaction management, and visual overlays. This plugin simulates human-like mouse and keyboard inputs to interact with the game client, offering a range of functionalities from camera control to inventory management.

---

## **Features**

- **Camera Control**: Programmatically adjust the camera's yaw, pitch, and zoom levels to predefined settings or based on in-game events.
- **Mouse Simulation**: Emulate human-like mouse movements and clicks using Bezier curves and randomization for natural interaction.
- **Keyboard Simulation**: Simulate keyboard inputs for controlling in-game actions, including camera adjustments and player movements.
- **Interaction Management**: Monitor and log interactions between the player and other entities (NPCs, objects), providing real-time feedback.
- **Inventory Handling**: Scan the player's inventory, randomize item interaction points, and simulate mouse movements over inventory items.
- **Visual Overlays**: Display informative overlays showing camera information, player position, and mouse crosshairs for enhanced situational awareness.
- **Auto Login**: Optionally attempt to automatically log back into the game if logged out, streamlining the reconnection process.

---

## **Prerequisites**

Before installing and using the Flump Plugin, ensure you have the following:

- **RuneLite Client**: Download and install the latest version of the [RuneLite](https://runelite.net/) client.
- **Java Development Kit (JDK)**: JDK 8 or higher is required for building and running the plugin. You can download it from [here](https://www.oracle.com/java/technologies/javase-downloads.html).

---

## **Installation**

### **1. Clone the Repository**

```bash
git clone https://github.com/cameronguthrie/flump-plugin.git
```

### **2. Import the Project**

- Open your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse).
- Import the project as a Maven project:
  - In IntelliJ IDEA: `File` > `Open` > Select the `pom.xml` file in the cloned repository.
  - In Eclipse: `File` > `Import` > `Existing Maven Projects` > Browse to the project directory.

### **3. Build the Plugin**

- Open a terminal in the project directory.
- Run the following command to build the plugin:

  ```bash
  mvn clean install
  ```

- Upon successful build, the plugin JAR file will be located in the `target` directory.

### **4. Add the Plugin to RuneLite**

- Locate the compiled plugin JAR file in the `target` directory (e.g., `flump-plugin-1.0.0.jar`).
- Copy the JAR file.
- Navigate to the RuneLite plugins directory:
  - On Windows: `%userprofile%\.runelite\plugins`
  - On macOS/Linux: `~/.runelite/plugins`
- Paste the JAR file into the `plugins` directory.
- Restart the RuneLite client.

---

## **Configuration**

After installing the plugin:

1. **Enable the Plugin**:

   - Open RuneLite.
   - Click on the wrench icon to open the configuration panel.
   - Scroll down to find the **Flump Plugin**.
   - Ensure the plugin is enabled.

2. **Configure Plugin Settings**:

   - Click on the **Flump Plugin** configuration gear icon.
   - Adjust the settings according to your preferences:
     - **Auto Login**: Enable to allow the plugin to attempt automatic login when logged out.
     - **Camera Info**: Enable to display an overlay with camera positional information.
     - **Player Position**: Enable to display an overlay with the player's world position.
     - **Kill NPC ID**: Specify the NPC ID for interaction (functionality placeholder).

---

## **Usage**

### **Commands**

The Flump Plugin provides custom commands that can be entered into the game chat:

- **Adjust Camera**:

  ```plaintext
  ::camera
  ```

  - Adjusts the camera to predefined yaw, pitch, and zoom settings.

- **Simulate Inventory Hover**:

  ```plaintext
  ::inventory
  ```

  - Simulates hovering over a random inventory slot.

### **Overlays**

- **Camera Information Overlay**:

  - Displays the camera's current pitch, yaw, and zoom levels.
  - Enable this overlay in the plugin configuration under **Camera Info**.

- **Player Position Overlay**:

  - Shows the player's current world coordinates (X, Y) and plane (Z-axis level).
  - Enable this overlay in the plugin configuration under **Player Position**.

- **Mouse Crosshair Overlay**:

  - Renders crosshairs or lines at the mouse position for visual feedback.
  - This overlay is always active when the plugin is enabled.

---

## **Development**

### **Project Structure**

- **`src/main/java/com/flump/`**: Contains all the Java source files for the plugin.

  - **Controllers**:
    - `CameraController.java`: Manages camera adjustments.
    - `MouseController.java`: Handles mouse input simulation.
    - `KeyboardController.java`: Handles keyboard input simulation.

  - **Managers**:
    - `InteractionManager.java`: Manages interactions between entities.
    - `InventoryManager.java`: Handles inventory scanning and interaction.

  - **Overlays**:
    - `CameraInfoOverlay.java`: Displays camera information.
    - `MouseControllerOverlay.java`: Renders mouse position overlays.
    - `PlayerPositionOverlay.java`: Displays player position information.

  - **Utilities**:
    - `MathStuff.java`: Provides utility mathematical functions.
    - `MessageManager.java`: Manages in-game message display.

  - **Plugin Entry Point**:
    - `TestPlugin.java`: The main plugin class that integrates all components.

  - **Configuration**:
    - `TestConfig.java`: Defines user-configurable settings for the plugin.

### **Building the Plugin**

- **Using Maven**:

  - Ensure you have Maven installed on your system.
  - Run the following command in the project directory:

    ```bash
    mvn clean package
    ```

  - The compiled plugin JAR file will be generated in the `target` directory.

### **Testing**

- **Unit Testing**:

  - Implement unit tests using a testing framework like JUnit to validate individual components.
  - Place test classes in the `src/test/java/` directory following the same package structure.

- **Manual Testing**:

  - Install the plugin in the RuneLite client as described in the installation section.
  - Test each feature individually:
    - Verify camera adjustments with the `::camera` command.
    - Check mouse simulation over inventory slots with the `::inventory` command.
    - Observe overlays for correctness and performance.

- **Debugging**:

  - Use your IDE's debugging tools to set breakpoints and inspect variables.
  - Utilize logging (e.g., `log.info()`, `System.out.println()`) to output runtime information.

---

## **Contribution**

At this time, the Flump Plugin is a personal project, and contributions are not being accepted. However, feedback and suggestions are welcome.

---

## **License**

**All Rights Reserved**

The Flump Plugin and its source code are the proprietary property of the owner. Unauthorized copying, distribution, modification, or use of this software is strictly prohibited without express permission from the owner.

---

## **Disclaimer**

- **Compliance with Game Policies**:

  - The use of this plugin is intended for personal and educational purposes only.
  - Users are responsible for ensuring that they comply with Old School RuneScape's terms of service, rules, and any applicable guidelines.
  - The developer is not responsible for any consequences arising from the use of this plugin.

- **No Warranty**:

  - This software is provided "as is," without warranty of any kind, express or implied.
  - The developer does not guarantee that the plugin will function uninterrupted or error-free.

---

## **Contact**

For questions, suggestions, or feedback, please contact:

- **GitHub**: [Cameron Guthrie](https://github.com/cameronguthrie)

---


## **Additional Considerations**

- **Future Development**:

  - Additional features such as customizable hotkeys, advanced interaction scripts, integration with other plugins.
  - Open-sourcing the project in the future to foster community collaboration (subject to license changes).

---

## **Acknowledgments**

- **RuneLite**: Thanks to the RuneLite open-source project for providing a powerful and extensible client platform.
