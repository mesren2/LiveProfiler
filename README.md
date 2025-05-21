# LiveProfiler
 Real-time in-game performance profiler for tracking how long a process takes to complete.
 
![image](https://github.com/user-attachments/assets/b5de5b2d-3d2f-415f-9c49-37e671bfa9b0)
# Download as mod (1.21.5)
 [Releases page](https://github.com/mesren2/LiveProfiler/releases)
# Use as dependency (mod developers)
## 1. Use jitpack to add to dependancies:
``Repositories block:``
```groovy
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
```
``Dependencies block:``
```groovy
	dependencies {
	        implementation 'com.github.mesren2:LiveProfiler:1.0'
	}
 ```
## 2. Create a feature
Make a feature class with a register() func:
```java
    public static void register() {
        LiveProfiler.begin("MyFeature");
        // feature process goes here
        LiveProfiler.end("MyFeature");
    }
```
In your ``ClientModInitializer()`` function, add:
```java
// Replace with (all if more than one) your features
TestFeatures.register();
```

## Building yourself
Download the source code and open it in [IntelliJ Idea](https://www.jetbrains.com/idea/] (or preferred java ide.)
Go to ``gradle sidebar > tasks > build > build``
The output file will be in ``build > libs``.
