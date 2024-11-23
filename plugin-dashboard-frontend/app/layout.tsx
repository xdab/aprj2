import type { ReactNode } from "react";
import { StoreProvider } from "./StoreProvider";
import { Nav } from "./components/Nav";

import WebSocketProvider from "./PacketStreamProvider";
import "./styles/globals.css";
import styles from "./styles/layout.module.css";

interface Props {
  readonly children: ReactNode;
}

export default function RootLayout({ children }: Props) {
  return (
    <StoreProvider>
      <WebSocketProvider />

      <html lang="en">
        <body>
          <section className={styles.container}>
            <Nav />

            <header className={styles.header}>
              aprj2 dashboard
            </header>

            <main className={styles.main}>{children}</main>

            <footer className={styles.footer}>
              SO5DZ 2024
            </footer>
          </section>
        </body>
      </html>
    </StoreProvider>
  );
}
