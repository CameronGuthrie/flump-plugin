# **Flump Plugin for RuneLite**

[![Flump Plugin](https://i.ytimg.com/vi/Df1-nVvbUK0/maxresdefault.jpg?)](http://www.youtube.com/watch?v=Df1-nVvbUK0 "OSRS Automation Plugin")

## **Overview**

The **Flump Plugin** is a custom plugin developed for the RuneLite client, aiming to enhance the Old School RuneScape gameplay experience by providing advanced automation features, interaction management, and visual overlays. This plugin simulates human-like mouse and keyboard inputs to interact with the game client, offering a range of functionalities from camera control to inventory management.

**Repository Location**: [https://github.com/CameronGuthrie/flump-plugin](https://github.com/CameronGuthrie/flump-plugin)

## **Features**

- **Camera Control**: Programmatically adjust the camera's yaw, pitch, and zoom levels to predefined settings or based on in-game events.
- **Mouse Simulation**: Emulate human-like mouse movements and clicks using Bezier curves and randomization for natural interaction.
- **Keyboard Simulation**: Simulate keyboard inputs for controlling in-game actions, including camera adjustments and player movements.
- **Interaction Management**: Monitor and log interactions between the player and other entities (NPCs, objects), providing real-time feedback.
- **Inventory Handling**: Scan the player's inventory, randomize item interaction points, and simulate mouse movements over inventory items.
- **Visual Overlays**: Display informative overlays showing camera information, player position, and mouse crosshairs for enhanced situational awareness.
- **Auto Login**: Optionally attempt to automatically log back into the game if logged out, streamlining the reconnection process.

## **Prerequisites**

Before installing and using the Flump Plugin, ensure you have the following:

- **RuneLite Client**: Download and install the latest version of the [RuneLite](https://runelite.net/) client.
- **Java Development Kit (JDK) 11 (Temurin)**:
  - The project requires JDK 11 (Temurin) for building and running.
  - You can download it from [Adoptium (formerly AdoptOpenJDK)](https://adoptium.net/):
    - Choose **Temurin** as the distribution.
    - Select **Version 11**.
    - Download the appropriate installer for your operating system.
  - **Note**: Ensure that the `JAVA_HOME` environment variable is set to point to your JDK 11 (Temurin) installation.

## **Installation**

### **1. Clone the Repository**

You can clone the repository directly from version control within IntelliJ IDEA:

1. **Open IntelliJ IDEA**.
2. **Get from Version Control**:
   - On the Welcome screen, click **Get from Version Control**.
   - Alternatively, go to **VCS** -> **Get from Version Control** in the menu bar.
3. **Enter Repository URL**:
   - Paste the repository URL: [https://github.com/CameronGuthrie/flump-plugin](https://github.com/CameronGuthrie/flump-plugin)
4. **Choose Directory**:
   - Select the directory where you want to clone the project.
5. **Clone**:
   - Click **Clone** to download the project.

### **2. Open the Project in IntelliJ IDEA**

- After cloning, IntelliJ IDEA will automatically open the project.
- If prompted to import the Gradle project, select **Import Gradle Project**.

## **Configuration**

### **1. Install Lombok Plugin**

The project uses Lombok to reduce boilerplate code. To ensure Lombok works correctly, install the Lombok plugin in IntelliJ IDEA:

1. **Install Lombok Plugin**:
   - Go to **File** -> **Settings** (or **IntelliJ IDEA** -> **Preferences** on macOS).
   - Navigate to **Plugins**.
   - Search for **Lombok** in the Marketplace.
   - Click **Install**.
2. **Enable Annotation Processing**:
   - Go to **File** -> **Settings** -> **Build, Execution, Deployment** -> **Compiler** -> **Annotation Processors**.
   - Check **Enable annotation processing**.
3. **Restart IntelliJ IDEA**:
   - Restart the IDE to apply changes.

### **2. Configure Java Development Kit (JDK)**

Ensure that your project is set to use JDK 11 (Temurin), and the `JAVA_HOME` environment variable points to your JDK 11 installation.

#### **Set `JAVA_HOME` Environment Variable**

- **Windows**:
  - Right-click **This PC** -> **Properties** -> **Advanced system settings** -> **Environment Variables**.
  - Under **System variables**, click **New**.
  - Set **Variable name** to `JAVA_HOME`.
  - Set **Variable value** to the path of your JDK 11 (Temurin) installation (e.g., `C:\Program Files\Eclipse Adoptium\jdk-11.0.12.7-hotspot`).
  - Add `%JAVA_HOME%\bin` to your **Path** variable.
- **macOS/Linux**:
  - Open terminal.
  - Edit your shell profile file (`~/.bash_profile`, `~/.bashrc`, or `~/.zshrc`):
    ```bash
    export JAVA_HOME=/path/to/your/jdk-11-temurin
    export PATH=$JAVA_HOME/bin:$PATH
    ```
  - Source the profile or restart the terminal:
    ```bash
    source ~/.bash_profile
    ```

#### **Set Project SDK in IntelliJ IDEA**

1. **Open Project Structure**:
   - Go to **File** -> **Project Structure**.
2. **Set Project SDK**:
   - Under **Project Settings** -> **Project**, set **Project SDK** to **11** (Temurin).
   - Set **Project language level** to **11 - Local variable syntax for lambda parameters, etc.**.
3. **Set Module SDK**:
   - Under **Project Settings** -> **Modules**.
   - Select your module.
   - Ensure the **Module SDK** is set to **11** (Temurin).

#### **Ensure Gradle Uses JDK 11**

1. **Set Gradle JVM in IntelliJ IDEA**:
   - Go to **File** -> **Settings** -> **Build, Execution, Deployment** -> **Build Tools** -> **Gradle**.
   - Under **Gradle JVM**, select **JDK 11 (Temurin)**.
2. **Set Gradle to Use JAVA_HOME**:
   - In your project's `gradle.properties` file, add:
     ```properties
     org.gradle.java.home=/path/to/your/jdk-11-temurin
     ```
     - Replace `/path/to/your/jdk-11-temurin` with the actual path.
     - Use forward slashes `/` or double backslashes `\\` in the path.

### **3. Set Up Run Configuration**

To build and run the plugin within IntelliJ IDEA, you need to create a new run configuration:

1. **Create New Run Configuration**:
   - Go to **Run** -> **Edit Configurations**.
   - Click the **+** button and select **Application**.
2. **Configure Run Configuration**:
   - **Name**: Set to any name you prefer (e.g., `Flump Plugin`).
   - **Main class**: Set to `com.flump.PluginLauncher`.
   - **Use classpath of module**: Select your module (ensure it's using Java 11 (Temurin)).
   - **VM options**: Add `-ea` to enable assertions.
   - **Program arguments**: Add `--debug --developer-mode` to enable debugging and developer mode in RuneLite.
   - **Modify options**:
     - Click on **Modify options** dropdown.
     - Ensure **Add VM options** is checked.
   - **Other settings**: Leave other options as default.
3. **Apply and Save**:
   - Click **Apply**, then **OK** to save the run configuration.

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
    - `PluginLauncher.java`: Launches the plugin within the RuneLite client.

  - **Configuration**:
    - `TestConfig.java`: Defines user-configurable settings for the plugin.

### **Building and Running the Plugin**

Since the project uses Gradle for building, follow these steps to build and run the plugin:

1. **Build the Project**:

   - Open the **Gradle** tool window in IntelliJ IDEA.
   - Navigate to **Tasks** -> **build**.
   - Double-click **build** to compile the project.

2. **Run the Plugin**:

   - Use the run configuration you set up earlier.
   - Click the **Run** button (green arrow) next to the run configuration dropdown.
   - The RuneLite client will start with your plugin loaded.

3. **Modify VM Options (If Necessary)**:

   - In your run configuration, ensure that **Add VM options** is checked in the **Modify options** dropdown.
   - VM options should include `-ea` for enabling assertions.

4. **Program Arguments**:

   - The program arguments `--debug --developer-mode` allow you to run RuneLite in developer mode with debugging enabled.

## **Contribution**

At this time, the Flump Plugin is a personal project, and contributions are not being accepted. However, feedback and suggestions are welcome. Please feel free to reach out with suggestions.

## **License**

**All Rights Reserved**

The Flump Plugin and its source code are the proprietary property of the owner. Unauthorized copying, distribution, modification, or use of this software is strictly prohibited without express permission from the owner.

## **Disclaimer**

- **Compliance with Game Policies**:

  - The use of this plugin is intended for personal and educational purposes only.
  - Users are responsible for ensuring that they comply with Old School RuneScape's terms of service, rules, and any applicable guidelines.
  - The developer is not responsible for any consequences arising from the use of this plugin.

- **No Warranty**:

  - This software is provided "as is," without warranty of any kind, express or implied.
  - The developer does not guarantee that the plugin will function uninterrupted or error-free.

## **Contact**

For questions, suggestions, or feedback, please contact:

- **GitHub**: [CameronGuthrie](https://github.com/CameronGuthrie)

**Note**: Replace placeholders like `your.email@example.com` with your actual contact information.

## **Additional Notes**

- **JDK 11 (Temurin)**:

  - The project requires JDK 11 (Temurin) for compatibility with RuneLite.
  - Ensure that `JAVA_HOME` is set to point to your JDK 11 installation.
  - Download JDK 11 (Temurin) from [Adoptium](https://adoptium.net/).

- **Gradle Build Tool**:

  - The project uses Gradle for building and dependency management.
  - All references to Maven have been removed from the instructions.

- **IntelliJ IDEA Project Import**:

  - The project can be easily imported into IntelliJ IDEA using the **Get from Version Control** feature.
  - This streamlines the setup process and ensures all project configurations are correctly loaded.

- **Run Configuration Details**:

  - **Module Classpath**:
    - Ensure the module classpath is set correctly to include all necessary dependencies.
  - **Main Class**:
    - Set to `com.flump.PluginLauncher` to launch the plugin.
  - **VM Options**:
    - Use `-ea` to enable assertions, which can help with debugging.
  - **Program Arguments**:
    - Include `--debug --developer-mode` to enable debugging features and developer mode in RuneLite.

- **Lombok Installation**:

  - Lombok is essential for reducing boilerplate code in the project.
  - Installing the Lombok plugin in IntelliJ IDEA and enabling annotation processing ensures that the project compiles correctly.

## **Additional Considerations**

- **Future Development**:

  - Additional features such as customizable hotkeys, advanced interaction scripts, integration with other plugins.
  - Open-sourcing the project in the future to foster community collaboration (subject to license changes).

## **Acknowledgments**

- **RuneLite**: Thanks to the RuneLite open-source project for providing a powerful and extensible client platform.
