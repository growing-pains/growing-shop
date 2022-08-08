package com.example.growingshop.acceptance.restDocs;

import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class Description {
    private List<Value> values = new ArrayList<>();

    public List<FieldDescriptor> generateDescription() {
        return values.stream().map(value -> {
            FieldDescriptor fieldDescriptor = fieldWithPath(value.field).description(value.description);

            if (value.type != null) {
                fieldDescriptor.type(value.type);
            }

            if (value.ignore) {
                fieldDescriptor.ignored();
            }

            return fieldDescriptor;
        }).collect(Collectors.toList());
    }

    public static class DescriptionBuilder {
        private Description description = new Description();

        public DescriptionBuilder add(String field, String description, String type) {
            this.description.values.add(new Value(field, description, type, false));

            return this;
        }

        public DescriptionBuilder add(String field, String description) {
            return this.add(field, description, null);
        }

        public DescriptionBuilder addIgnore(String field) {
            this.description.values.add(new Value(field, null, null, true));

            return this;
        }

        public Description build() {
            return description;
        }
    }

    private static class Value {
        private final String field;
        private final String description;
        private final String type;
        private final boolean ignore;

        private Value(String field, String description, String type, boolean ignore) {
            this.field = field;
            this.description = description;
            this.type = type;
            this.ignore = ignore;
        }
    }
}
