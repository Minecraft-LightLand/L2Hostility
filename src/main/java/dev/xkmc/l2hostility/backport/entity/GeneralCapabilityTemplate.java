package dev.xkmc.l2hostility.backport.entity;


import dev.xkmc.l2library.util.code.Wrappers;

public class GeneralCapabilityTemplate<E, T extends GeneralCapabilityTemplate<E, T>> {

	public T getThis() {
		return Wrappers.cast(this);
	}

	public T check() {
		return getThis();
	}

}
