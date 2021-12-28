#include <stdio.h>
#include <string.h>
#include "linked_list.h"

void print_linked_list(struct LinkedListNode *list) {
    while (list != NULL) {
        printf("%s%s", (char *) list->data, list->next != NULL ? " -> " : "");
        list = list->next;
    }
    printf("\n");
}

int finder(struct LinkedListNode* node, int index, const void *target) {
    return strcmp(target, node->data) == 0 ? 2 : 0;
}

int main() {
    struct LinkedListNode *list = linked_list_new("Hello");
    print_linked_list(list);

    linked_list_freeDataEnabled(1);

    linked_list_insertAt(linked_list_new("1", linked_list_new("2")), list, 0);
    print_linked_list(list);

    linked_list_insertAt(linked_list_new("3"), list, 1);
    print_linked_list(list);

    linked_list_insertAt(linked_list_new("4"), list, 1);
    print_linked_list(list);

    linked_list_append(linked_list_new("5"), list);
    print_linked_list(list);

    linked_list_connect_before(&list, linked_list_new("NEW", linked_list_new("HI")), linked_list_new("TEST"));
    print_linked_list(list);

    // REMOVE "HI"
    linked_list_removeAt(list, 2);
    print_linked_list(list);

    // INSERT CUSTOM AFTER NEXT ELEMENT OF NEW (+2)
    linked_list_insert(linked_list_new("CUSTOM"), list, "NEW", finder);
    print_linked_list(list);
}
