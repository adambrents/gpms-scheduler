$JDKPath = "C:\Program Files\Java\jdk-21.0.2"
$AppPath = "C:\Projects\java\C868_Capstone_Adam_Brents\out\artifacts\C868___Capstone___Music_School_Scheduler_jar\C868 - Capstone - Music School Scheduler.jar"
$OutputDir = "C:\Projects\java\C868_Capstone_Adam_Brents\package"
$InputDir = "C:\Projects\java\C868_Capstone_Adam_Brents\input"
$Name = "MusicSchoolScheduler"
$MainClass = "main.Program"

# VM Options
$VMOptions = "-Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 --module-path=""./lib/dependencies/sdk/javafx-sdk-21.0.2/lib"" --add-modules=javafx.controls,javafx.fxml"

# Try to close any processes that might be using files within the output directory
Get-Process | Where-Object { $_.MainWindowTitle -like "*$Name*" } | Stop-Process -Force -ErrorAction SilentlyContinue

# Wait a moment to ensure file handles are released
Start-Sleep -Seconds 2

# Ensure $InputDir exists and is empty
if (Test-Path $InputDir) {
    Remove-Item -Path "$InputDir\*" -Recurse -Force
} else {
    New-Item -Path $InputDir -ItemType Directory
}

# Attempt to remove $OutputDir more robustly
function Remove-OutputDir {
    try {
        Remove-Item -Path "$OutputDir\*" -Recurse -Force
    } catch {
        Write-Host "Waiting for files to be released..."
        Start-Sleep -Seconds 5
        Remove-Item -Path "$OutputDir\*" -Recurse -Force
    }
}

if (Test-Path $OutputDir) {
    Remove-OutputDir
} else {
    New-Item -Path $OutputDir -ItemType Directory
}

# Copy application JAR and dependencies to $InputDir
Copy-Item "C:\Projects\java\C868_Capstone_Adam_Brents\lib\*" $InputDir -Recurse
Copy-Item $AppPath $InputDir

# Use jpackage to create the application package
& "$JDKPath\bin\jpackage" --input $InputDir --name $Name --main-jar "C868 - Capstone - Music School Scheduler.jar" --main-class $MainClass --type app-image --dest $OutputDir --java-options $VMOptions
