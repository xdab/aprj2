"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

import styles from "../styles/layout.module.css";

export const Nav = () => {
  const pathname = usePathname();

  return (
    <nav className={styles.nav}>
      <Link
        className={`${styles.link} ${pathname === "/" ? styles.active : ""}`}
        href="/"
      >
        Packets
      </Link>
      <Link
        className={`${styles.link} ${pathname === "/verify" ? styles.active : ""
          }`}
        href="/verify"
      >
        Verify
      </Link>
    </nav>
  );
};
