
ServerEvents.tags('item', event => {
    event.add('l2hostility:trait_item', 'kubejs:test_trait')
    event.add('l2hostility:trait_item', 'kubejs:max_health')
    event.add('l2hostility:trait_item', 'kubejs:mining_fatigue')
    event.add('l2hostility:trait_item', 'kubejs:hunger')
})

ServerEvents.tags('l2hostlity:trait', event => {
    event.add('l2hostility:potion_trait', 'kubejs:mining_fatigue')
    event.add('l2hostility:potion_trait', 'kubejs:hunger')
})

L2Hostility.newAttackListener()
    .subscribeDamage(e => {
        try {
            var target = e.getTarget()
            var source = e.getSource()
            var cap = L2Hostility.of(target);
            if (cap != null && cap.getTraitLevel("kubejs:test_trait") > 0) {
                if (L2Hostility.entityIs(target, "minecraft:slime")) {
                    if (L2Hostility.sourceIs(source, "#minecraft:is_fire")) {
                        e.addDealtModifier(DamageModifier.multTotal(0.5), ID.kjs("test_trait"))
                    }
                }
                if (L2Hostility.entityIs(target, "minecraft:zombie")) {
                    if (L2Hostility.sourceIs(source, "#neoforge:is_magic")) {
                        e.addDealtModifier(DamageModifier.multTotal(1.5), ID.kjs("test_trait"))
                    }
                    if (L2Hostility.sourceIs(source, "#minecraft:is_projectile")) {
                        e.addDealtModifier(DamageModifier.multTotal(0.5), ID.kjs("test_trait"))
                    }
                }
            }
        } catch (ex) {
            console.log(ex)
        }
    }).subscribeCreateSource(e => {
        try {
            if (e.sourceIs( "#l2damagetracker:direct")) {
                if (e.getAttacker().getMainHandItem().is("minecraft:echo_shard")) {
                    e.enable("bypass_magic");
                }
                if (e.getAttacker().getMainHandItem().is("minecraft:bedrock")) {
                    e.setTo("minecraft:out_of_world");
                }
            }
            console.log(e.getOriginal())
            if (e.sourceIs("#minecraft:is_projectile")) {
                if (e.getAttacker().getOffHandItem().is("minecraft:echo_shard")) {
                    e.setTo("minecraft:out_of_world");
                }
            }
        } catch (ex) {
            console.log(ex)
        }
    }).register("test", 10000)