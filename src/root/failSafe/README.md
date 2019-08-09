# FailSafe.execute
- A utility that allows execution of Java program in fail safe manner i.e. if program is terminated it would restart itself again. Implementation is based on IPC, achieved through Java I/O Stream, Process and Runtime API.
- See (root.failSafe.usageExample) for usage 

# FailSafe.keepHosted
- Makes a network service discoverable to specified host 
- Deploy a network service on specified port
- If port binding fails, silently attempt binding to different port 
- Run network service
- Repeat cycle and re-deploy 