package io.mk8bk;

enum CommandSessionRequirement {
    LOGGED_OUT,
    CASHIER,
    MANAGER,
    CASHIER_OR_MANAGER,
    CASHIER_OR_MANAGER_OR_LOGGED_OUT
}
