import type { Metadata } from "next";
import { PacketStream } from "./PacketStream";

export default function IndexPage() {
  return <PacketStream />;
}

export const metadata: Metadata = {
  title: "aprj2 dash: Packet stream",
};
