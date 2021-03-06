/*
 * Copyright 2016 Ecole des Mines de Saint-Etienne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.thesmartenergy.sparql.generate.jena.iterator.library;

import com.github.thesmartenergy.sparql.generate.jena.SPARQLGenerate;
import com.github.thesmartenergy.sparql.generate.jena.iterator.IteratorFunctionBase1;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.sparql.expr.nodevalue.NodeValueNode;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Iterator function
 * <a href="http://w3id.org/sparql-generate/iter/CSVHeaders">iter:CSVHeaders</a>
 * iterates over the cells in the header row.
 *
 * <ul>
 * <li>Param 1: (csv) is the CSV document with a header line.</li>
 * </ul>
 *
 * @author Noorani Bakerally <noorani.bakerally at emse.fr>
 */
public class ITER_CSVHeaders extends IteratorFunctionBase1 {

    private static final Logger LOG = LoggerFactory.getLogger(ITER_CSVHeaders.class);

    public static final String URI = SPARQLGenerate.ITER + "CSVHeaders";

    private static final String datatypeUri = "http://www.iana.org/assignments/media-types/text/csv";

    @Override
    public List<List<NodeValue>> exec(NodeValue csv) {
        if (csv.getDatatypeURI() != null
                && !csv.getDatatypeURI().equals(datatypeUri)
                && !csv.getDatatypeURI().equals("http://www.w3.org/2001/XMLSchema#string")) {
            LOG.debug("The URI of NodeValue1 MUST be <" + datatypeUri + ">"
                    + "or <http://www.w3.org/2001/XMLSchema#string>."
            );
        }
        List<NodeValue> nodeValues = new ArrayList<>();

        DocumentBuilderFactory builderFactory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {

            String sourceCSV = String.valueOf(csv.asNode().getLiteralLexicalForm());

            InputStream is = new ByteArrayInputStream(sourceCSV.getBytes("UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            CsvMapReader mapReader = new CsvMapReader(br, CsvPreference.STANDARD_PREFERENCE);
            String headers_str[] = mapReader.getHeader(true);

            for (String header : headers_str) {
                Node node = NodeFactory.createLiteral(header);
                NodeValueNode nodeValue = new NodeValueNode(node);
                nodeValues.add(nodeValue);
            }
            return new ArrayList<>(Collections.singletonList(nodeValues));
        } catch (Exception ex) {
            LOG.debug("No evaluation for " + csv, ex);
            throw new ExprEvalException("No evaluation for " + csv, ex);
        }
    }

}
