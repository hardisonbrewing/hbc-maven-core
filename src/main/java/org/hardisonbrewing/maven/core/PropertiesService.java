/**
 * Copyright (c) 2010 Martin M Reed
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.hardisonbrewing.maven.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesService {

    protected PropertiesService() {

        // do nothing
    }

    public static final void storeProperties( Properties properties, String filePath ) {

        try {
            properties.store( new FileOutputStream( filePath ), null );
        }
        catch (IOException e) {
            JoJoMojo.getMojo().getLog().error( "Unable to write properties file: " + filePath );
            throw new IllegalStateException( e.getMessage() );
        }
    }

    public static final Properties loadProperties( String filePath ) {

        File file = new File( filePath );
        if ( !file.exists() ) {
            return null;
        }

        Properties properties = new Properties();

        try {
            properties.load( new FileInputStream( file ) );
        }
        catch (IOException e) {
            JoJoMojo.getMojo().getLog().error( "Unable to read properties file: " + filePath );
            throw new IllegalStateException( e.getMessage() );
        }

        return properties;
    }

    public static final Properties getProperties() {

        Properties properties = new Properties();

        // do these first so execution properties can overwrit
        properties.putAll( ProjectService.getProject().getProperties() );

        // these properties come from command line
        properties.putAll( JoJoMojo.getMojo().getMavenSession().getExecutionProperties() );
        return properties;
    }

    public static final String getProperty( String key ) {

        String value = JoJoMojo.getMojo().getMavenSession().getExecutionProperties().getProperty( key );
        if ( value != null ) {
            return value;
        }
        return ProjectService.getProject().getProperties().getProperty( key );
    }

    public static final File getPropertyAsFile( String key ) {

        String filePath = getProperty( key );
        if ( filePath == null ) {
            return null;
        }
        if ( filePath.endsWith( File.separator ) ) {
            filePath = filePath.substring( 0, filePath.length() - 1 );
        }
        return new File( filePath );
    }

    public static final boolean getPropertyAsBoolean( String key ) {

        String property = getProperty( key );
        if ( property == null ) {
            return false;
        }
        return "true".equalsIgnoreCase( property );
    }
}
