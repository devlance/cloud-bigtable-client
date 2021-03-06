/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.bigtable.grpc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.api.client.util.NanoClock;
import com.google.bigtable.v1.BigtableServiceGrpc;
import com.google.bigtable.v1.CheckAndMutateRowRequest;
import com.google.bigtable.v1.MutateRowRequest;
import com.google.bigtable.v1.Mutation;
import com.google.bigtable.v1.Mutation.SetCell;
import com.google.bigtable.v1.ReadRowsRequest;
import com.google.bigtable.v1.RowRange;
import com.google.cloud.bigtable.config.BigtableOptions;
import com.google.cloud.bigtable.config.RetryOptions;
import com.google.cloud.bigtable.config.RetryOptionsUtil;
import com.google.cloud.bigtable.grpc.io.ChannelPool;
import com.google.cloud.bigtable.grpc.io.ClientCallService;
import com.google.cloud.bigtable.grpc.io.RetryingCall;
import com.google.common.base.Predicate;
import com.google.protobuf.ByteString;
import com.google.protobuf.ServiceException;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.MethodDescriptor;

@RunWith(JUnit4.class)
@SuppressWarnings("unchecked")
public class BigtableDataGrpcClientTests {

  @Mock
  ChannelPool channelPool;

  @Mock
  Channel channel;

  @SuppressWarnings("rawtypes")
  @Mock
  ClientCall clientCall;

  @Mock
  ExecutorService executorService;

  @Mock
  ScheduledExecutorService retryExecutorService;

  @Mock
  ClientCallService clientCallService;

  @Mock
  NanoClock nanoClock;

  BigtableDataGrpcClient underTest;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    RetryOptions retryOptions = RetryOptionsUtil.createTestRetryOptions(nanoClock);
    BigtableOptions options = new BigtableOptions.Builder().setRetryOptions(retryOptions).build();
    underTest = new BigtableDataGrpcClient(channelPool, executorService, retryExecutorService,
        options, clientCallService);
    when(channelPool.newCall(any(MethodDescriptor.class), any(CallOptions.class))).thenReturn(
      clientCall);
    when(channel.newCall(any(MethodDescriptor.class), any(CallOptions.class))).thenReturn(
      clientCall);
  }

  @Test
  public void testRetyableMutateRow() throws ServiceException {
    MutateRowRequest request = MutateRowRequest.getDefaultInstance();
    underTest.mutateRow(request);
    verify(clientCallService).blockingUnaryCall(any(RetryingCall.class), same(request));
  }

  @Test
  public void testRetyableMutateRowAsync() {
    MutateRowRequest request = MutateRowRequest.getDefaultInstance();
    underTest.mutateRowAsync(request);
    verify(clientCallService).listenableAsyncCall(any(RetryingCall.class), same(request));
  }

  @Test
  public void testRetyableCheckAndMutateRow() throws ServiceException {
    CheckAndMutateRowRequest request = CheckAndMutateRowRequest.getDefaultInstance();
    underTest.checkAndMutateRow(request);
    verify(clientCallService).blockingUnaryCall(any(RetryingCall.class), same(request));
  }

  @Test
  public void testRetyableCheckAndMutateRowAsync() {
    CheckAndMutateRowRequest request = CheckAndMutateRowRequest.getDefaultInstance();
    underTest.checkAndMutateRowAsync(request);
    verify(clientCallService).listenableAsyncCall(any(RetryingCall.class), same(request));
  }

  @Test
  public void testMutateRowPredicate() {
    Predicate<MutateRowRequest> predicate = BigtableDataGrpcClient.IS_RETRYABLE_MUTATION;
    assertFalse(predicate.apply(null));

    MutateRowRequest.Builder request = MutateRowRequest.newBuilder();
    assertTrue(predicate.apply(request.build()));

    request.addMutations(
        Mutation.newBuilder().setSetCell(SetCell.newBuilder().setTimestampMicros(-1)));
    assertFalse(predicate.apply(request.build()));
  }

  @Test
  public void testCheckAndMutateRowPredicate() {
    Predicate<CheckAndMutateRowRequest> predicate =
        BigtableDataGrpcClient.IS_RETRYABLE_CHECK_AND_MUTATE;
    assertFalse(predicate.apply(null));

    CheckAndMutateRowRequest.Builder request = CheckAndMutateRowRequest.newBuilder();
    assertTrue(predicate.apply(request.build()));

    request.addTrueMutations(
        Mutation.newBuilder().setSetCell(SetCell.newBuilder().setTimestampMicros(-1)));
    assertFalse(predicate.apply(request.build()));

    request.clearTrueMutations();
    request.addFalseMutations(
        Mutation.newBuilder().setSetCell(SetCell.newBuilder().setTimestampMicros(-1)));
    assertFalse(predicate.apply(request.build()));
  }

  @Test
  public void testSingleRowRead() {
    ReadRowsRequest request =
        ReadRowsRequest.newBuilder().setRowKey(ByteString.copyFrom(new byte[0])).build();
    underTest.readRows(request);
    verify(channelPool, times(1)).newCall(eq(BigtableServiceGrpc.METHOD_READ_ROWS),
      same(CallOptions.DEFAULT));
  }

  @Test
  public void testMultiRowRead() {
    ReadRowsRequest request =
        ReadRowsRequest.newBuilder().setRowRange(RowRange.getDefaultInstance()).build();
    underTest.readRows(request);
    verify(channelPool, times(1)).newCall(eq(BigtableServiceGrpc.METHOD_READ_ROWS),
      same(CallOptions.DEFAULT));
  }
}
