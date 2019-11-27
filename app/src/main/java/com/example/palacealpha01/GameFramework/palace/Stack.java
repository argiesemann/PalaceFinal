/**
 * @formatter:off
 */
package com.example.palacealpha01.GameFramework.palace;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Maximilian
 * <p>
 * This class is a Stack data structure, with some extra methods specifically designed for our needs.
 * We plan to use this for the discard pile, because the discard pile is most easily represented as
 * as stack.
 */
public class Stack implements Serializable
{
	/**
	 * @author Maximilian
	 * <p>
	 * This class is only used inside the Stack.java class. The stack object created by the Stack.java
	 * class is essentially a linked list where the nodes are always prepended at the head of the list.
	 * Therefore, a node class was needed.
	 */
	private class StackNode
	{
		private Pair data;
		private StackNode next;

		/**
		 * default constructor for the StackNode class
		 * @param data
		 */
		public StackNode(Pair data)
		{
			this.data = data;
			this.next = null;
		}//END: StackNode() constructor

		@Override
		public boolean equals(Object obj)
		{
			if (! (obj instanceof StackNode))
				return false;

			if (! this.data.equals(((StackNode) obj).data))
				return false;

			return true;
		}
	}//END: StackNode class

	private StackNode head; // This is the head/top of the Stack. It is the first Node in the stack's linked list.

	/**
	 * default constructor for the Stack.java class
	 */
	public Stack()
	{
		this.head = null;
	}//END: Stack() default constructor

	public Stack(Pair[] initial_nodes, boolean in_order)
	{
		this.head = null;
		if (in_order)
			for (Pair p : initial_nodes)
				this.push(p);
		else
			for (int i = initial_nodes.length - 1; i >= 0; i--)
				this.push(initial_nodes[i]);
	}//END: Stack() constructor

	public Stack(ArrayList<Pair> initial_nodes, boolean in_order)
	{
		this.head = null;
		if (in_order)
			for (Pair p : initial_nodes)
				this.push(p);
		else
			for (int i = initial_nodes.size() - 1; i >= 0; i--)
				this.push(initial_nodes.get(i));
	}//END: Stack() constructor

	/**
	 * copy constructor for the Stack.java class
	 * This is the wrapper method for the copy_constructor() recursive method.
	 * @param that
	 */
	public Stack(Stack that)
	{
		this.head = null;
		this.copy_constructor(that.head);
	}//END: Stack() copy constructor

	/**
	 * This is a recursive method.
	 * @param current
	 */
	private void copy_constructor(StackNode current)
	{
		if (current == null)
			// NOTE: Exit recursion loop here.
			return;

		// NOTE: the is the only recursive call within this function
		this.copy_constructor(current.next);

		// NOTE: work is done post-order
		this.push(current.data);
	}//END: copy_constructor() recursive method

	public void push(Pair data)
	{
		StackNode new_head = new StackNode(data);
		new_head.next = this.head;
		this.head = new_head;
	}//END: push() method

	public Pair pop()
	{
		if (this.head == null)
			return null;
		Pair rtn = this.head.data;
		this.head = this.head.next;
		return rtn;
	}//END: pop() method

	public Pair peek()
	{
		if (this.head == null)
			return null;
		return this.head.data;
	}//END: peek() method

	public boolean are_next_four_equal()
	{
		if (this.head == null)
			return false;
		final int FOUR = 4;
		StackNode current = this.head;
		for (int i = 1; i <= (FOUR - 1); i++)
		{
			if (current.next == null)
				return false;
			if (current.data.get_card().get_rank() != current.next.data.get_card().get_rank())
				return false;
			current = current.next;
		}
		return true;
	}//END: are_next_four_equal() method

	public boolean is_empty()
	{
		return (this.head == null);
	}//END: is_empty() method

	public void clear()
	{
		while (! this.is_empty())
			this.pop();
	}//END: clear() method

	/**
	 * This is the wrapper function for the toString() recursive method.
	 * @return
	 */
	@Override
	public String toString()
	{
		return to_string_rec("", this.head);
	}

	/**
	 * This is a wrapper function for the equals_rec() method.
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (! (obj instanceof Stack))
			return false;

		return equals_rec(this.head, ((Stack) obj).head);
	}

	/**
	 * This is a recursive function.
	 * @param this_current
	 * @param that_current
	 * @return
	 */
	private static boolean equals_rec(StackNode this_current, StackNode that_current)
	{
		// if both Stacks are empty, or both Stacks have the same number of StackNodes
		if (this_current == null && that_current == null)
			// NOTE: recursive loop exit
			return true;

		// if both Stack's size are not equal
		if ((this_current == null) ^ (that_current == null)) // '^' X-OR logic operator
			// NOTE: recursive loop exit
			return false;

		// if at any point, both StackNode's data is not equal
		if (! this_current.data.equals(that_current.data))
			// NOTE: recursive loop exit
			return false;

		// NOTE: this is the single recursive within this function
		return equals_rec(this_current.next, that_current.next);
	}

	/**
	 * This is a recursive method.
	 * @param s
	 * @param current
	 */
	private static String to_string_rec(String s, StackNode current)
	{
		if (current == null)
			// NOTE: we exit recursion loop here
			return s;

		s += current.data.toString() + "\n";

		// NOTE: this is the single recursive call within this function
		return to_string_rec(s, current.next);
	}//END: to_string() recursive method

	/**
	 * This is the wrapper method for the size_rec() recursive method.
	 * @return
	 */
	public int size()
	{
		return size_rec(this.head);
	}

	/**
	 * This is a recursive method.
	 * @param current
	 * @return
	 */
	private static int size_rec(StackNode current)
	{
		if (current == null)
			// NOTE: recursive loop exit
			return 0;
		// NOTE: this is the single recursive call within this function
		return 1 + size_rec(current.next);
	}
}//END: Stack class
