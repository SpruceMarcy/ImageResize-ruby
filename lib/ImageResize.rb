$:.unshift(File.dirname(__FILE__)) unless
  $:.include?(File.dirname(__FILE__)) || $:.include?(File.expand_path(File.dirname(__FILE__)))

require 'ImageResize/Image'

module ImageResize
  VERSION = '0.1.0cd mb0'
end
