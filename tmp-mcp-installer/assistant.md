# Workflow: Add default remote MCP servers to a single config file on macOS (Cursor, Windsurf, Claude Code or Codex CLI) via direct file URL

Purpose
- Resolve app base to configure (Cursor, Windsurf, Claude Code, Codex)
- Locate (or create) the single MCP configuration file based on the app base.
- Read existing MCP servers.
- Read default MCP servers from one JSON file.
- Add only missing defaults (idempotent). Do not modify existing entries.
- Support both JSON (Cursor, Windsurf, Claude) and TOML (Codex CLI) configuration formats.
- Configure custom rule (mcp-usage.md) for MCP authority-first development in the appropriate location for each app.

Assumptions about defaults file
- The file at default-mcps.json is a JSON object with a top-level mcpServers object.
- Each key in mcpServers is the MCP ID; each value is the server definition for a remote MCP.

Rules
- Prefer a dedicated config file at the app's base directory
- Preserve existing MCP entries; add only missing IDs.
- Idempotent: re-running produces "no changes" if already applied.

Steps

1) Resolve app base (macOS only)
- **Systematically detect the current environment** using this priority order:

  **A) Check current working context (MOST RELIABLE):**
  - Check parent process of current shell:
    - `PARENT_PROCESS=$(ps -p $(ps -o ppid= -p $$) -o comm=)`
    - If contains "Cursor Helper" → **Cursor**
    - If contains "Windsurf" → **Windsurf** 
    - If contains "claude" → **Claude Code**
    - If contains "codex" → **Codex CLI**
  - Fallback to general process check:
    - `ps aux | grep -i cursor | grep -v grep` → if found: **Cursor**
    - `ps aux | grep -i windsurf | grep -v grep` → if found: **Windsurf**
    - `ps aux | grep -i claude | grep -v grep` → if found: **Claude Code**
    - `ps aux | grep -i codex | grep -v grep` → if found: **Codex CLI**
  
  **B) Check environment variables:**
  - `$CURSOR_USER_CONFIG_DIR` or `$CURSOR_SESSION_ID` → **Cursor**
  - `$WINDSURF_SESSION_ID` → **Windsurf** 
  - `$CLAUDE_SESSION_ID` → **Claude Code**

  **C) Verify base directories exist:**
  - `~/.cursor/` exists → **Cursor**
  - `~/.codeium/windsurf/` or `~/.codeium/` exists → **Windsurf**
  - `which claude` and `~/.claude/` exists → **Claude Code**
  - `~/.codex/` exists → **Codex CLI**

- **Decision logic:**
  - If exactly ONE environment is detected: proceed with that flow
  - If MULTIPLE environments detected: **stop and ask user to specify which IDE to configure**
  - If NO environment detected: **stop and report no supported IDE found**
  
- **Validation before proceeding:**
  - Confirm the detected environment by checking both process AND directory existence
  - Log the chosen environment clearly: "Selected environment: [CURSOR/WINDSURF/CLAUDE/CODEX]"
  - Proceed to corresponding step: Cursor → step 2, Windsurf → step 3, Claude Code → step 4, Codex CLI → step 5

2) Cursor flow (JSON)
- Base path: ~/.cursor
- Resolve destination config file
  - Destination = <Base>/mcp.json
  - Fallback search under <Base> (first existing): mcp_config.json, mcp/config.json, mcp/servers.json
  - Ensure parent directory exists; if creating a new file, initialize to: { "mcpServers": {} }
- Load current configuration
  - Parse as standard JSON
  - Ensure a top-level mcpServers object; if missing, create it
- Fetch defaults
  - Read ./tmp-mcp-installer/default-mcps.json and parse JSON; ensure defaults.mcpServers is an object
- Compute differences (idempotent)
  - EXISTING_URLS = set of URLs from:
    - Every entry with a "url" field
    - Plus args[0] where command == "mcp-remote-proxy"
  - DEFAULT_ENTRIES = defaults.mcpServers entries
  - Normalize URLs when comparing (trim trailing slashes)
  - MISSING_ENTRIES = DEFAULT_ENTRIES whose URL is not in EXISTING_URLS
  - DUPLICATE_URLS = DEFAULT_ENTRIES whose URL is already in EXISTING_URLS
- Write changes
  - For each missing default entry:
    - If the default contains "url": write it as { "url": "..." }
    - If the default contains command "mcp-remote-proxy": keep command/args as provided
  - Insert into current.mcpServers using the original key; preserve existing entries
  - Write pretty JSON (2-space indentation, newline at EOF)
- Validation (Cursor)
  - File is valid JSON and contains a top-level mcpServers object
  - Each remote server is either { "url": string } OR { "command": "mcp-remote-proxy", "args": [url, "--transport", "http", ...] }
  - No duplicate URLs after normalization
- Configure custom rule (Cursor)
  - Copy mcp-usage.md to .cursor/rules in the current project directory
  - Ensure the file uses .mdc extension for Cursor compatibility
  - Rule will be automatically loaded by Cursor on next startup
- Example (Cursor)
  ```json
  {
    "mcpServers": {
      "example-api": { "url": "https://api.example.com/mcp" },
      "local-service": { "url": "http://localhost:8080/mcp" },
      "remote-tool": {
        "command": "mcp-remote-proxy",
        "args": ["https://api.example.com/mcp", "--transport", "http"]
      }
    }
  }
  ```

3) Windsurf flow (JSON)
- Base path: ~/.codeium/windsurf
- Resolve destination config file
  - Destination = <Base>/mcp_config.json
  - Fallback search under <Base> (first existing): mcp.json, mcp/config.json, mcp/servers.json
  - Ensure parent directory exists; if creating a new file, initialize to: { "mcpServers": {} }
- Load current configuration
  - Parse as JSON; ensure a top-level mcpServers object
- Fetch defaults
  - Read ./tmp-mcp-installer/default-mcps.json and parse JSON; ensure defaults.mcpServers is an object
- Compute differences (idempotent)
  - EXISTING_URLS = args[0] of entries where command == "mcp-remote-proxy"
  - DEFAULT_ENTRIES = defaults.mcpServers entries
  - Normalize URLs when comparing (trim trailing slashes)
  - MISSING_ENTRIES = DEFAULT_ENTRIES whose URL is not in EXISTING_URLS
  - DUPLICATE_URLS = DEFAULT_ENTRIES whose URL is already in EXISTING_URLS
- Write changes
  - Convert each missing default to Windsurf remote-proxy form:
    - If default has "url": write as { "command": "mcp-remote-proxy", "args": [url, "--transport", "http"] }
    - If default already has command "mcp-remote-proxy": keep as is
  - Insert into current.mcpServers using the original key; preserve existing entries
  - Write pretty JSON (2-space indentation, newline at EOF)
  - Replicate file on ~/.codeium/mcp_config.json. 
    - If it exists, add the MCPs to the JSON
- Validation (Windsurf)
  - File is valid JSON and contains a top-level mcpServers object
  - Remote servers use { "command": "mcp-remote-proxy", "args": [url, ...] }
  - No duplicate URLs by args[0] after normalization
  - File is present and complete on both ~/.codeium/windsurf/mcp_config.json and ~/.codeium/mcp_config.json paths
- Configure custom rule (Windsurf)
  - Copy mcp-usage.md to .windsurf/rules in the current project directory
  - Ensure the file uses .md extension for Windsurf compatibility
  - Rule will be automatically loaded by Windsurf on next startup
- Command execution guidelines
  - Run complete commands that are already validated and error-free before use.
  - Avoid wrapping commands with `eval` in any shell (bash, zsh, sh, fish, etc.); prefer direct terminal invocations or dedicated Python scripts when automation is required.
- Temporary file handling constraints
  - Never create temporary files outside ./tmp-mcp-installer and avoid absolute paths for temporary artifacts.
  - Do not use heredocs whose destination depends on variables inside quoted shell commands; prefer fixed paths with printf or standard redirection.
  - Enable set -u and validate critical variables (e.g., WORK, TMPD) before any redirection. Abort if they are empty to avoid writing to /.
  - Abort immediately if any path-building variable is empty to prevent unintended writes to root directories.
  - Default to dry-run operations that emit artifacts into ./tmp-mcp-installer/out; do not touch $HOME or /tmp without explicit authorization.
- Example (Windsurf)
  ```json
  {
    "mcpServers": {
      "local-tool-server": {
        "command": "npx",
        "args": ["-y", "@example/mcp-tool-server"],
        "env": {}
      },
      "proxy-service": {
        "command": "mcp-remote-proxy",
        "args": ["https://api.example.com/mcp", "--transport", "http"]
      },
      "docker-service": {
        "command": "docker",
        "args": ["run", "-i", "--rm", "-e", "API_TOKEN", "example/mcp-docker-server"],
        "env": { "API_TOKEN": "<YOUR_API_TOKEN>" }
      }
    }
  }
  ```

4) Claude Code flow (CLI)
- Base path: ~
- Manage configuration via Claude CLI; do not edit files directly.
- List current servers
  - Run: claude mcp list
  - Parse output to collect EXISTING_URLS
- Fetch defaults
  - Read ./tmp-mcp-installer/default-mcps.json and parse JSON; ensure defaults.mcpServers is an object
- Compute differences (idempotent)
  - DEFAULT_ENTRIES = defaults.mcpServers entries
  - Extract URL from each default:
    - If default has "url": use it
    - If default has command "mcp-remote-proxy": use args[0]
  - Normalize URLs (trim trailing slashes)
  - MISSING_ENTRIES = DEFAULT_ENTRIES whose URL is not in EXISTING_URLS
  - DUPLICATE_URLS = DEFAULT_ENTRIES whose URL is already in EXISTING_URLS
- Write changes
  - For each missing default entry with key = {name} and URL = {url}:
    - Run: claude mcp add --scope user --transport http {name} {url}
  - This installs servers for all Claude projects (user scope)
- Validation (Claude Code)
  - Run: claude mcp list 
  - Confirm all added {name} entries appear and URLs match after normalization
  - No duplicate URLs
- Configure custom rule (Claude Code)
  - Create/update Claude.md in the current project root directory
  - Copy mcp-usage.md content to Claude.md
  - Claude automatically reads Claude.md as custom instructions
  - Note: Claude.md must be in the project root (not in .claude/)
- Example commands (Claude Code)
  ```bash
  claude mcp list 
  claude mcp add --transport http --scope user example-api https://api.example.com/mcp
  claude mcp add --transport http --scope user local-service https://api.example.com/mcp
  ```

5) Codex CLI flow (TOML)
- Base path: ~/.codex
- Resolve destination config file
  - Destination = <Base>/config.toml
  - Fallback search under <Base> (first existing): mcp.json, mcp_config.json, .claude.json, config.toml, mcp/config.json, mcp/servers.json, User/settings.json (use only if it contains "mcpServers"; note: JSONC with comments)
  - Ensure parent directory exists; if creating a new file, include a [mcp_servers] section
- Load current configuration
  - Parse as TOML; ensure there is a [mcp_servers] section; if missing, create it
- Fetch defaults
  - Read ./tmp-mcp-installer/default-mcps.json and parse JSON; ensure defaults.mcpServers is an object
- Compute differences (idempotent)
  - EXISTING_URLS = first element of args arrays in [mcp_servers.*] entries where command == "mcp-remote-proxy"
  - DEFAULT_ENTRIES = defaults.mcpServers entries
  - Normalize URLs when comparing (trim trailing slashes)
  - MISSING_ENTRIES = DEFAULT_ENTRIES whose URL is not in EXISTING_URLS
  - DUPLICATE_URLS = DEFAULT_ENTRIES whose URL is already in EXISTING_URLS
- Write changes
  - For each missing default entry, create a section [mcp_servers.{key}] (replace blanks with _):
    - command = "mcp-remote-proxy"
    - args = [url, "--transport", "http"]
  - Write formatted TOML with proper indentation
  - Do not delete any TOML configuration unrelated to MCPs
- Validation (Codex CLI)
  - File is valid TOML and contains [mcp_servers]
  - Each [mcp_servers.{name}] has command = "mcp-remote-proxy" and args[0] == url
  - {name} must not contain blank spaces (use underscores)
  - Note: do not use backslashes when adding the MCP configurations
- Configure custom rule (Codex CLI)
  - Copy mcp-usage.md content to AGENTS.md in the current project directory
  - Codex automatically reads AGENTS.md as custom instructions
- Example (Codex CLI)
  ```toml
  [mcp_servers.genai_mcp]
  command = "mcp-remote-proxy"
  args = ["https://genai-integrator-mcp-mcp.melioffice.com/mcp", "--transport", "http"]

  [mcp_servers.fury]
  command = "mcp-remote-proxy"
  args = ["https://fury-mcp.melioffice.com/mcp", "--transport", "http"]

  [mcp_servers.example_api]
  command = "mcp-remote-proxy"
  args = ["https://api.example.com/mcp", "--transport", "http"]

  [mcp_servers.local_service]
  command = "mcp-remote-proxy"
  args = ["http://localhost:8080/mcp", "--transport", "http"]
  ```

6) Cleanup (always)
- Remove the entire ./tmp-mcp-installer directory running `rm -rf ./tmp-mcp-installer`.

7) Report (always)
- App and resolved Base directory.
- Destination file path used (or created).
- Entries added (MISSING_ENTRIES with their URLs and assigned keys).
- Entries already present (DUPLICATE_URLS with their existing keys).
- Custom rule configuration:
  - Rule file location (e.g., ~/.cursor/rules/mcp-usage.mdc)
  - Whether rule was created or updated
  - Confirmation that rule will be loaded on next app startup
- Any warnings (e.g., invalid default entries, settings.json not modified).

Edge cases and handling
- Invalid JSON or missing mcpServers in defaults: abort and report.
- Destination file invalid format:
  - If it is invalid JSON/TOML, report and stop unless a safe migration is possible.
  - For TOML files, ensure proper parsing and validate [mcp_servers] section structure.
- Secrets in defaults (e.g., headers.Authorization): avoid logging secret values; allow placeholders (e.g., ${TOKEN}).
- TOML-specific considerations:
  - Preserve existing TOML structure and comments when possible.
  - Ensure proper escaping of special characters in URLs and strings.

Completion criteria
- Base app is resolved to exactly one environment (Cursor, Windsurf, Claude Code, or Codex CLI).
- Cursor:
  - <Base>/mcp.json exists and is valid JSON with a top-level mcpServers object.
  - Each remote server is either { "url": string } or { "command": "mcp-remote-proxy", "args": [url, "--transport", "http", ...] }.
  - All defaults from defaults.mcpServers are present; URLs are unique after normalization (trim trailing slashes).
  - .cursor/rules/mcp-usage.mdc exists in the current project directory with custom rule content.
- Windsurf:
  - <Base>/mcp_config.json exists and is valid JSON with a top-level mcpServers object. Copy file at .windsurf/mcp_config.json exists and is set too.
  - Remote servers use { "command": "mcp-remote-proxy", "args": [url, ...] } format.
  - All defaults are present; args[0] URLs are unique after normalization.
  - .windsurf/rules/mcp-usage.md exists in the current project directory with custom rule content.
- Claude Code:
  - User scope contains all defaults: `claude mcp list --scope user` shows an entry for each default key and the URL matches after normalization (trim trailing slashes).
  - No duplicate URLs in user scope.
  - Claude.md exists in the project root with custom rule content.
- Codex CLI:
  - <Base>/config.toml exists and contains a [mcp_servers] section.
  - Each default has a corresponding [mcp_servers.{name}] with command = "mcp-remote-proxy" and args = [url, "--transport", "http"].
  - Section names use underscores (no spaces) and all args[0] URLs are unique after normalization.
  - AGENTS.md exists in the current project root directory.
- Existing entries remain unchanged for all environments.
- Re-running produces no changes for all environments.
- Cleanup completed: temporary files removed and ./tmp-mcp-installer deleted when safe.

Quick checklist
- [ ] Resolve Base (Cursor/Windsurf/Claude/Codex CLI) and destination file.
- [ ] Ensure destination exists (create appropriate config file format if needed).
- [ ] Load current config.
- [ ] Fetch and parse defaults from default-mcps.json.
- [ ] Compute MISSING; if none, report and exit.
- [ ] Write in appropriate format (JSON/TOML); **validate** that has a valid format.
- [ ] Configure custom rule (copy mcp-usage.md to appropriate location with correct name/extension).
- [ ] Remove the ./tmp-mcp-installer directory completely.
- [ ] Report summary including custom rule configuration and warnings.
