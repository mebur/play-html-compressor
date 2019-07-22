/**
 * Play HTML Compressor
 * <p>
 * LICENSE
 * <p>
 * This source file is subject to the new BSD license that is bundled
 * with this package in the file LICENSE.md.
 * It is also available through the world-wide-web at this URL:
 * https://github.com/fkoehler/play-html-compressor/blob/master/LICENSE.md
 */
package com.github.fkoehler.play.xmlcompressor.fixtures.java;

import com.github.fkoehler.play.xmlcompressor.XMLCompressorFilter;
import play.filters.gzip.GzipFilter;
import play.http.DefaultHttpFilters;
import play.mvc.EssentialFilter;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides the default XML compressor filter with a Gzip filter.
 */
public class WithGzipFilter extends DefaultHttpFilters {

    private XMLCompressorFilter xmlCompressorFilter;
    private GzipFilter gzip;

    @Inject
    public WithGzipFilter(XMLCompressorFilter xmlCompressorFilter, GzipFilter gzip) {
        this.xmlCompressorFilter = xmlCompressorFilter;
        this.gzip = gzip;
    }

    @Override
    public List<EssentialFilter> getFilters() {
        List<EssentialFilter> l = new LinkedList<EssentialFilter>();
        l.add(gzip.asJava());
        l.add(xmlCompressorFilter.asJava());
        return l;
    }
}
