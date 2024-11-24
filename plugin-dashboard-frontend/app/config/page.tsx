import type { Metadata } from "next";
import Config from "./Config";

export default function IndexPage() {
  return <Config />;
}

export const metadata: Metadata = {
  title: "aprj2 dashboard: Config view",
};
