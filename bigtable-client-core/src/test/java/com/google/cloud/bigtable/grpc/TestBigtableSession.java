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

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.cloud.bigtable.config.BigtableOptions;

@SuppressWarnings({"resource","unused"})
public class TestBigtableSession {

  private static final String PROJECT_ID = "project_id";
  private static final String ZONE_ID = "zone_id";
  private static final String CLUSTER_ID = "cluster_id";
  private static final String USER_AGENT = "user_agent";

  private static void createSession(
      String projectId, String zoneId, String clusterId, String userAgent) throws IOException {
    BigtableSession ignored =
        new BigtableSession(new BigtableOptions.Builder()
          .setProjectId(projectId)
          .setZoneId(zoneId)
          .setClusterId(clusterId)
          .setUserAgent(userAgent)
          .build(), null, null, null);
  }

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testNoProjectIdBigtableOptions() throws IOException {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage(BigtableSession.PROJECT_ID_EMPTY_OR_NULL);
    createSession(null, ZONE_ID, CLUSTER_ID, USER_AGENT);
  }

  @Test
  public void testNoZoneIdBigtableOptions() throws IOException {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage(BigtableSession.ZONE_ID_EMPTY_OR_NULL);
    createSession(PROJECT_ID, null, CLUSTER_ID, USER_AGENT);
  }

  @Test
  public void testNoClusterIdBigtableOptions() throws IOException {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage(BigtableSession.CLUSTER_ID_EMPTY_OR_NULL);
    createSession(PROJECT_ID, ZONE_ID, null, USER_AGENT);
  }

  @Test
  public void testNoUserAgentBigtableOptions() throws IOException {
    expectedException.expect(IllegalArgumentException.class);
    expectedException.expectMessage(BigtableSession.USER_AGENT_EMPTY_OR_NULL);
    createSession(PROJECT_ID, ZONE_ID, CLUSTER_ID, null);
  }

}
