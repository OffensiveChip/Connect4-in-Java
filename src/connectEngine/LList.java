package connectEngine;

class LList {
Linked head, tail;
int size = 0;
	void newNode(int nVal) {
		if(head == null) 
		{
			Linked list = new Linked(nVal);
			head = tail = list;
		}
		else {
			Linked list = new Linked(nVal);
			tail.next = list;
			list.prev = tail;
			tail = list;
		}
		size++;
	}
	void printList() {
		Linked temp = head;
		while(temp != null) {
			System.out.println(temp.val);
			temp = temp.next;
		}
//		System.out.println(temp.val);
	}
	void destroyLink() {
		Linked temp = tail;
		while(temp.prev != null) {
			temp = temp.prev;
			temp.next = null;
		}
		head = null;
		temp = null;
		tail = null;
		size = 0;
	}
	
	boolean checkWin() {
		Linked temp = tail;
		int counter = 0;
		while(temp.prev != null) {
			temp = temp.prev;
			if(temp.val == temp.next.val && temp.val != 0) {
				counter++;
			}
			else if (temp.val != temp.next.val) {
				counter = 0;
			}
			if(counter >= 3){
				return true;
			}	
		}
		return false;
	}

	/*
	boolean checkWin() {
		Linked temp = head;
		int counter = 0;
		if (size < 4) {
			return false;
		}
		else {
			while(temp.next != null) {
				if (temp.val == temp.next.val) {
					counter++;
				}
				else if(temp.val != temp.next.val) {
					counter = 0;
				}
				if(counter >=4)
					return true;
				temp = temp.next;
			}
			
			return false;
		}
	}
*/
}
