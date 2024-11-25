import type { Metadata } from "next";
import { PacketBuilder } from "./PacketBuilder";

export default function IndexPage() {
  return <PacketBuilder />;
}

export const metadata: Metadata = {
  title: "aprj2 dash: Packet builder",
};
