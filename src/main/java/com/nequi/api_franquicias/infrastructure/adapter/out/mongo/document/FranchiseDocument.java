package com.nequi.api_franquicias.infrastructure.adapter.out.mongo.document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "franchises")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FranchiseDocument {
    @Id
    private String id;
    private String name;
    private List<BranchDocument> branches;
}
