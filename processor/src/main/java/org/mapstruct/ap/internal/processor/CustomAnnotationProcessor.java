/**
 *  Copyright 2012-2016 Gunnar Morling (http://www.gunnarmorling.de/)
 *  and/or other contributors as indicated by the @authors tag. See the
 *  copyright.txt file in the distribution for a full listing of all
 *  contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mapstruct.ap.internal.processor;

import javax.lang.model.element.TypeElement;
import java.util.Collections;

import org.mapstruct.ap.internal.model.Annotation;
import org.mapstruct.ap.internal.model.Mapper;
import org.mapstruct.ap.internal.model.common.Type;
import org.mapstruct.ap.internal.util.MapperConfiguration;

/**
 * Mapstruct annotation processor for adding custom annotations to the generated converter implementation.
 *
 * @author Jakub Stonavsky
 */
public class CustomAnnotationProcessor implements ModelElementProcessor<Mapper, Mapper> {

    private static final String ANNOTATION_TYPE_KEY = "mapstruct.custom.annotation.type";

    private static final String ANNOTATION_VALUE_KEY = "mapstruct.custom.annotation.value";

    private static final int PRIORITY = 1100;

    private static final String TYPE = "cdi";

    public Mapper process( ProcessorContext context, TypeElement mapperTypeElement, Mapper mapper ) {
        final String componentModel = MapperConfiguration.getInstanceOn( mapperTypeElement )
                .componentModel( context.getOptions() );
        if ( !TYPE.equalsIgnoreCase( componentModel ) ) {
            return mapper;
        }

        final String annotationTypeClass = System.getProperty( ANNOTATION_TYPE_KEY );
        final String annotationValue = System.getProperty( ANNOTATION_VALUE_KEY );
        if ( annotationTypeClass != null ) {
            System.clearProperty( ANNOTATION_TYPE_KEY );
            System.clearProperty( ANNOTATION_VALUE_KEY );
            final Type annotationType = context.getTypeFactory().getType( annotationTypeClass );
            final Annotation annotation = annotationValue == null ?
                    new Annotation( annotationType ) :
                    new Annotation( annotationType, Collections.singletonList( annotationValue ));
            mapper.addAnnotation( annotation );
        }

        return mapper;
    }

    public int getPriority() {
        return PRIORITY;
    }

}
