package dev.xkmc.l2hostility.backport.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * only entities will be automatically attached by default for efficiency
 */
public class GeneralCapabilityHolder<E extends ICapabilityProvider, T extends GeneralCapabilityTemplate<E, T>> {

	public static final Map<ResourceLocation, GeneralCapabilityHolder<?, ?>> INTERNAL_MAP = new ConcurrentHashMap<>();

	public final Capability<T> capability;
	public final ResourceLocation id;
	public final Class<T> holder_class;
	public final Class<E> entity_class;
	public final Supplier<T> sup;

	private final Predicate<E> pred;


	public GeneralCapabilityHolder(ResourceLocation id, Capability<T> capability, Class<T> holder_class, Supplier<T> sup,
								   Class<E> entity_class, Predicate<E> pred) {
		this.id = id;
		this.capability = capability;
		this.holder_class = holder_class;
		this.entity_class = entity_class;
		this.sup = sup;
		this.pred = pred;
		INTERNAL_MAP.put(id, this);
	}

	public T get(E e) {
		return e.getCapability(capability).resolve().get().check();
	}

	public boolean shouldHaveCap(E entity){
		return pred.test(entity);
	}

	public boolean isProper(E entity) {
		return entity.getCapability(capability).isPresent();
	}

	public GeneralCapabilitySerializer<E, T> generateSerializer(E entity) {
		return new GeneralCapabilitySerializer<>(this);
	}

}
