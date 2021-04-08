# GVault

A Gunpowder add-on which adds *vaults*, per-player end-chest-style inventories which can be accessed via commands.

## Commands

| Command                           | Permission node    | Action                                   |
|-----------------------------------|--------------------|------------------------------------------|
| `/vault <number>`                 | `gvault.viewVault` | Shows vault `#number` to yourself.       |
| `/vault showto <target> <number>` | `gvault.showTo`    | Shows `target` their own vault `#number` |
| `/vault spy <target> <number>`    | `gvault.spy`       | Shows you `target`'s vault `#number`     |

## Config

```yaml
vaultCount: 128 # How many vaults does each player have access to?
isDoubleChest: yes # Are vaults double-size?
```