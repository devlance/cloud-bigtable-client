// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/protobuf/timestamp.proto

package com.google.protobuf;

public final class TimestampProto {
  private TimestampProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_Timestamp_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_Timestamp_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\037google/protobuf/timestamp.proto\022\017googl" +
      "e.protobuf\"+\n\tTimestamp\022\017\n\007seconds\030\001 \001(\003" +
      "\022\r\n\005nanos\030\002 \001(\005B*\n\023com.google.protobufB\016" +
      "TimestampProtoP\001\240\001\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_google_protobuf_Timestamp_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_google_protobuf_Timestamp_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_google_protobuf_Timestamp_descriptor,
        new java.lang.String[] { "Seconds", "Nanos", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
