StartupEvents.registry('item', e => {
    e.create('test_trait', 'trait')
    e.create('max_health', 'trait')
    e.create('mining_fatigue', 'trait')
    e.create('hunger', 'trait')
})

StartupEvents.registry('l2hostility:trait', e => {
    e.create('test_trait', 'basic')
    e.create('max_health', 'attribute').attribute('max_health_trait', 'minecraft:generic.max_health', 0.2, "+%")
    e.create('mining_fatigue', 'effect').fixedDuration('mining_fatigue', 200)
    e.create('hunger', 'effect').fixedLevel('hunger', 200, 3)
})

