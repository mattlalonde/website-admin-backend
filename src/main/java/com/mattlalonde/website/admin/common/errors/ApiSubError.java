package com.mattlalonde.website.admin.common.errors;

import lombok.Data;

@Data
abstract class ApiSubError {
    protected String type;

    ApiSubError() {
        this.type = "ValidationError";
    }
}
