/*
 * Copyright 2017 École des Mines de Saint-Étienne.
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
package com.github.thesmartenergy.sparql.generate.jena.stream;

import com.github.thesmartenergy.sparql.generate.jena.SPARQLGenerate;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;
import org.apache.jena.atlas.web.ContentType;
import org.apache.jena.riot.RDFLanguages;

/**
 *
 * @author maxime.lefrancois
 */
public class RDFFileTypeDetector extends FileTypeDetector {

    public String probeContentType(Path path) throws IOException {
        if(path == null) {
            throw new IOException();
        }
        String fileName = path.getFileName().toString();
        if(fileName.endsWith(SPARQLGenerate.EXT)) {
            return SPARQLGenerate.MEDIA_TYPE;
        }
        ContentType ct = RDFLanguages.guessContentType(fileName);
        if(ct==null) {
            throw new IOException();
        }
        return ct.getContentType();
    }
}
