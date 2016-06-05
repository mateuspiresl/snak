package com.forbait.games.util;

public class Pair<A, B> {

	private A a;
	private B b;
	
	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	public A getA() {
		return this.a;
	}
	
	public B getB() {
		return this.b;
	}
	
	@Override
	public boolean equals(Object that)
	{
		if (this == that) return true;
		if (that == null || ! (this.getClass().isAssignableFrom(that.getClass()))) return false;
		
		Pair<?, ?> pair = (Pair<?, ?>) that;
		return this.a.equals(pair.a) && this.b.equals(pair.b);
	}
	
	@Override
	public String toString() {
		return "Pair { a: " + this.a + ", b: " + this.b + " }";
	}
	
	@Override
	public int hashCode() {
		return (this.a.hashCode() << 16) | (this.b.hashCode() & 0x0000FFFF);
	}
	
}
