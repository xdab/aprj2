import type { Metadata } from "next";
import { Packets } from "./Packets";

export default function IndexPage() {
  return <Packets />;
}

export const metadata: Metadata = {
  title: "aprj2 dashboard: Packet view",
};
