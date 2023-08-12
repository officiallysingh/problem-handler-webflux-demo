package com.ksoot.problem.demo.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.UncheckedIOException;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;

import reactor.core.scheduler.Schedulers;

public class FilePartUtils {

  public static InputStream getInputStreamSync(final FilePart filePart) {
    PipedOutputStream outputStream = new PipedOutputStream();
    PipedInputStream inputStream;
    try {
      inputStream = new PipedInputStream(outputStream);
    } catch (final IOException e) {
      throw new UncheckedIOException("IOException while preparing InputStream from FilePart", e);
    }
    DataBufferUtils.write(filePart.content(), outputStream).subscribeOn(Schedulers.boundedElastic()).doOnComplete(() -> {
      try {
        outputStream.close();
      } catch (final IOException ioException) {
      }
    }).subscribe(DataBufferUtils.releaseConsumer());
    return inputStream;
  }

  public static InputStream getInputStream(final FilePart filePart) {
    PipedOutputStream outputStream = new PipedOutputStream();
    PipedInputStream inputStream;
    try {
      inputStream = new PipedInputStream(outputStream);
    } catch (final IOException e) {
      throw new UncheckedIOException("IOException while preparing InputStream from FilePart", e);
    }
    DataBufferUtils.write(filePart.content(), outputStream).subscribeOn(Schedulers.boundedElastic()).doOnComplete(() -> {
      try {
        outputStream.close();
      } catch (final IOException ioException) {
      }
    }).subscribe(DataBufferUtils.releaseConsumer());
    return inputStream;
  }

  private FilePartUtils() {
    throw new IllegalStateException("Utility class, not supposed to be instantiated");
  }
}
