package com.forbait.games.util;

public abstract class Pair<A, B> {
	
	public abstract A getA();
	public abstract B getB();
	
	@Override
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (that == null || ! (this.getClass().isAssignableFrom(that.getClass()))) return false;
		
		Pair<?, ?> pair = (Pair<?, ?>) that;
		return getA().equals(pair.getA()) && this.getB().equals(pair.getB());
	}
	
	@Override
	public String toString() {
		return "Pair { a: " + getA() + ", b: " + getB() + " }";
	}
	
	@Override
	public int hashCode() {
		return (getA().hashCode() << 16 + getA().hashCode() & 0x0000FFFF)
				| (getB().hashCode() >> 16 + getB().hashCode() & 0x0000FFFF);
	}
	
}
