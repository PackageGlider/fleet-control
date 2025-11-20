.PHONY: help clean build install dev

help:
	@echo "Fleet Control - Make targets:"
	@echo "  make clean     - Clean build artifacts"
	@echo "  make build     - Build debug APK"
	@echo "  make install   - Install APK on connected device"
	@echo "  make dev       - Build and install (one command)"

clean:
	@echo "🧹 Cleaning build files..."
	@./gradlew clean
	@echo "✅ Clean completed"

build:
	@echo "🔧 Building debug APK..."
	@./gradlew assembleDebug
	@echo "✅ Debug APK built at: app/build/outputs/apk/debug/app-debug.apk"

install:
	@echo "📱 Installing on connected device..."
	@./gradlew installDebug
	@echo "✅ App installed on device"

dev: clean build install
