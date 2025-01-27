Pod::Spec.new do |spec|
    spec.name                     = 'iosComposePod'
    spec.version                  = '1.0'
    spec.homepage                 = 'https://github.com/softartdev/NoteDelight'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Common UI-kit for the NoteDelight app'
    spec.vendored_frameworks      = 'build/cocoapods/framework/iosComposeKit.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '14.1'
    spec.dependency 'SQLCipher', '4.5.4'
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':ios-compose-kit',
        'PRODUCT_MODULE_NAME' => 'iosComposeKit',
    }
                
    spec.script_phases = [
        {
            :name => 'Build iosComposePod',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
    spec.resources = ['build/compose/ios/iosComposeKit/compose-resources']
end