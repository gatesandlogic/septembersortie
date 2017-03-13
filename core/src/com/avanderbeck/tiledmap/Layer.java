package com.avanderbeck.tiledmap;

import java.util.Stack;

public class Layer {
	public Stack<Integer> data;
	public int height, width, x, y;
	public String name;
	public String type;
	public float opacity;
	public boolean visible;
}
