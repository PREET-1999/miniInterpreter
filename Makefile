# Directories
SRC_DIR = src/lox
BUILD_DIR = build

# Find all Java source files
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Convert source file paths to class file paths in build dir
CLASSES = $(patsubst $(SRC_DIR)/%.java,$(BUILD_DIR)/%.class,$(SOURCES))

# Default target
all: $(BUILD_DIR) $(CLASSES)

# Create build directory if it doesn't exist
$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

# Rule to compile each .java file into .class file
$(BUILD_DIR)/%.class: $(SRC_DIR)/%.java
	javac -d $(BUILD_DIR) $<

# Clean compiled files
clean:
	rm -rf $(BUILD_DIR)