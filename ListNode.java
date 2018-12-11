package edu.century.lifProject;

public class ListNode {
	
	private LifeNode data; 
	private ListNode link; 
	
	public ListNode(LifeNode data) {
		this.data = data;
		this.link = null;
	}

	public LifeNode getData() {
		return data;
	}

	public void removeNodeAfter() {
		link = link.link;
	}
	public void setData(LifeNode data) {
		this.data = data;
	}


	public ListNode getNextNode() {
		return link;
	}


	public void setNextNode(ListNode nextNode) {
		this.link = nextNode;
	}

	public void addNodeAfter(LifeNode element) {
		link = new ListNode(element);
	}
	
	public static int size(ListNode head) {
		ListNode cursor;
		int count = 0;
		
		for (cursor = head; cursor != null; cursor = cursor.getNextNode()) {
			count++;
		}
		
		return count;
	}
	
	public static int countNeighbors(ListNode head)
	{
		ListNode cursor;
		int neighbors = 0;
		
		for (cursor = head; cursor != null; cursor = cursor.getNextNode()) {
			neighbors += cursor.getData().getStatus(); 
		}
		
		return neighbors; 
	}

}
