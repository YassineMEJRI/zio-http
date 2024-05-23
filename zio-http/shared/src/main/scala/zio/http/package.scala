/*
 * Copyright 2021 - 2023 Sporta Technologies PVT LTD & the ZIO HTTP contributors.
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

package zio

import java.util.UUID

import zio.http.codec.PathCodec

package object http extends UrlInterpolator with MdInterpolator {

  /**
   * A smart constructor that attempts to construct a handler from the specified
   * value. If you have difficulty using this function, then please use the
   * constructors on [[zio.http.Handler]] directly.
   */
  def handler[H](handler: => H)(implicit h: ToHandler[H]): Handler[h.Env, h.Err, h.In, h.Out] =
    h.toHandler(handler)

  def handlerTODO(message: String): Handler[Any, Nothing, Any, Nothing] =
    handler(ZIO.dieMessage(message))

  def withContext[C](fn: => C)(implicit c: WithContext[C]): ZIO[c.Env, c.Err, c.Out] =
    c.toZIO(fn)

  def boolean(name: String): PathCodec[Boolean] = PathCodec.bool(name)
  def int(name: String): PathCodec[Int]         = PathCodec.int(name)
  def long(name: String): PathCodec[Long]       = PathCodec.long(name)
  def string(name: String): PathCodec[String]   = PathCodec.string(name)
  val trailing: PathCodec[Path]                 = PathCodec.trailing
  def uuid(name: String): PathCodec[UUID]       = PathCodec.uuid(name)

  val Root: PathCodec[Unit] = PathCodec.empty

  type RequestHandler[-R, +Err] = Handler[R, Err, Request, Response]

  type Client = ZClient[Any, Body, Throwable, Response]
  def Client: ZClient.type = ZClient

  /**
   * A channel that allows websocket frames to be written to it.
   */
  type WebSocketChannel = Channel[WebSocketChannelEvent, WebSocketChannelEvent]

  /**
   * A channel that allows websocket frames to be read and write to it.
   */
  type WebSocketChannelEvent = ChannelEvent[WebSocketFrame]
}
