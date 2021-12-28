# LinkedList

What is [LinkedList](https://geeksforgeeks.org/data-structures/linked-list/)?
> A linked list is a linear data structure, in which the elements are not stored at contiguous memory locations. The elements in a linked list are linked using pointers

# Usage

- include LinkedList header file
```c
#include "linked_list.h"
```

- Create the Head element :
```c
struct LinkedListNode *list = linked_list_new("Hello");
```

- Append
```c
linked_list_append(linked_list_new("Test"), list);
```

Result: Hello -> Test

- Push
```c
linked_list_push(linked_list_new("1", linked_list_new("2")), list);
```

Result: 1 -> 2 -> Hello -> Test

- Pop
```c
linked_list_pop(list);
```
Result: 2 -> Hello -> Test

- Insert
```c
linked_list_insertAt(linked_list_new("A"), list, 1);
```
Result: 2 -> A -> Hello -> Test

- Insert with Finder function
```c
// -1 = insert before target
// +1 = insert after target
// +2 = insert after the next position of target, ...
int finder(struct LinkedListNode* node, int index, const void *target) {
    return strcmp(target, node->data) == 0 ? 2 : 0;
}

linked_list_insert(linked_list_new("B"), list, "A", finder);
```
Result: 2 -> A -> Hello -> B -> Test

- Remove
```c
linked_list_removeAt(list, 2);
```
Result: 2 -> A -> B -> Test

- Connect
```c
linked_list_connect_before(&list, linked_list_new("NEW", linked_list_new("HI")), linked_list_new("TEST"));
```
Result: TEST -> NEW -> HI -> 2 -> A -> B -> Test

- Check out other functions such as `connect_after`, `pollLast`, `peekLast`, `removeRange`, `copy`, `toArray`, `indexOf`, `lastIndexOf`, `swap`, `set`, `find`, ...
