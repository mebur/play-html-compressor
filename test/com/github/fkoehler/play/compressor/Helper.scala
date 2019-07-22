/**
 * Play HTML Compressor
 *
 * LICENSE
 *
 * This source file is subject to the new BSD license that is bundled
 * with this package in the file LICENSE.md.
 * It is also available through the world-wide-web at this URL:
 * https://github.com/fkoehler/play-html-compressor/blob/master/LICENSE.md
 */
package com.github.fkoehler.play.compressor

import java.io.ByteArrayInputStream
import java.util.zip.GZIPInputStream

import akka.util.ByteString
import org.apache.commons.io.IOUtils

/**
 * Provides some test helpers.
 */
object Helper {

  /**
   * A helper function which unzips a gzipped byte array.
   *
   * @param data The data to unzip.
   * @return The unzipped data.
   */
  def gunzip(data: ByteString): ByteString = {
    ByteString(IOUtils.toByteArray(new GZIPInputStream(new ByteArrayInputStream(data.toArray))))
  }
}
