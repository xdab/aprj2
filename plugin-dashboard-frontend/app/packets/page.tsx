import type { Metadata } from "next";
import { Packets } from "./components/Packets";

export default function IndexPage() {
  return <Packets />;
}

export const metadata: Metadata = {
  title: "Redux Toolkit",
};
