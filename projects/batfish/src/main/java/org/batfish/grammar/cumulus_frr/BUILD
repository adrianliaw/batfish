package(default_visibility = ["//visibility:public"])

load("@batfish//skylark:pmd_test.bzl", "pmd_test")

java_library(
    name = "cumulus_frr",
    srcs = glob(
        ["**/*.java"],
        exclude = ["BUILD"],
    ),
    deps = [
        "//projects/batfish-common-protocol:common",
        "//projects/batfish/src/main/antlr4/org/batfish/grammar/cumulus_frr",
        "//projects/batfish/src/main/java/org/batfish/representation/cumulus",
        "//projects/batfish/src/main/java/org/batfish/representation/cumulus_interfaces",
        "@maven//:com_google_code_findbugs_jsr305",
        "@maven//:com_google_guava_guava",
        "@maven//:org_antlr_antlr4_runtime",
    ],
)

pmd_test(
    name = "pmd",
    lib = ":cumulus_frr",
)
