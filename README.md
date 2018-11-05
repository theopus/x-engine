```puml
    component Scheduler
    component TaskExecutor
    
    
    folder managers {
        component PlatformManager
        component LockManager
        component EventManager
    }
    
    component Injector
    
    proposer -> (ConcreteTask)
    
    Injector ..> managers: hold
    Injector -u-> ConcreteTask: inject
    ConcreteTask -> Scheduler
    Scheduler -> Scheduler: manage lifecycle
    
    Scheduler -d-> TaskExecutor: execute
```


```puml
component TaskComponent

EventManager -> TaskComponent
```

